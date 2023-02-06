<template>
  <a-layout-content style="padding: 0 250px">
    <div class="body">
      <a-space direction="horizontal" size="large">
        <a-input-search
            placeholder="输入待搜索名称"
            enter-button="Search"
            size="large"
            @search="onSearch"
        />
        <a-button type="primary" @click="addDownloadItem">
          新增
        </a-button>
      </a-space>
    </div>

    <!--    下载管理表格-->
    <a-table
        :columns="columns"
        :data-source="listData"
        :row-key="record => record.id"
        :pagination="pagination" @change="handleTableChange"
        :loading="loading"
        bordered>
      <template v-slot:bodyCell="{ column, record, index }">
        <template v-if="column.dataIndex === 'action'">
          <a-space size="small">
            <a-button type="link" @click="buttonEdit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="确认删除吗"
                ok-text="确认"
                cancel-text="取消"
                @confirm="buttonDelete(record.id)"
            >
              <a-button type="link">
                删除
              </a-button>
            </a-popconfirm>

          </a-space>
        </template>
      </template>
    </a-table>
  </a-layout-content>

  <!--  操作弹窗-->
  <a-modal
      title="下载表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form
        :model="downloadList"
        :label-col="{ span : 4 }"
    >
      <a-form-item label="名称">
        <a-input v-model:value="downloadList.name"/>
      </a-form-item>
      <a-form-item label="分类">
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="categoryData"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import {message} from 'ant-design-vue';
import axios from 'axios';
import {Tool} from "@/utils/tool";

export default defineComponent({
  components: {},
  name: "AdminDownload",
  setup: function () {
    const loading = ref(true);
    const listData = ref();
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    });


    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        width: '60%',
      },
      {
        title: '分类',
        dataIndex: 'categoryId1',
        width: '10%',
      },
      {
        title: '大小',
        dataIndex: 'size',
        width: '10%',
      },
      {
        title: '点赞数',
        dataIndex: 'downloadCount',
        width: '15%',
      },
      {
        title: '操作',
        dataIndex: 'action',
        width: '30%',
      },
    ];


    //-------------搜索框--------------]
    const onSearch = (searchValue: string) => {
      handleQuery({
        current: 1,
        pageSize: pagination.value.pageSize,
        name: searchValue,
      })
    };

    //-------------页面--------------
    /**
     * 新增按钮
     * 注: 这里不需要写具体的新增逻辑, 已经在对话框的"确认"按钮的逻辑中写过了
     */
    const addDownloadItem = () => {
      modalVisible.value = true;
      downloadList.value = {};  // 清空当前的数据信息
    };

    /**
     * 下载列表数据查询
     * @param p
     */
    const handleQuery = (p: any) => {
      axios.get("/downloadList/list", {
        params: {
          page: p.current,
          size: p.pageSize,
          name: p.name,
        }
      }).then((response) => {

        if (response.data.success) {  // 判断后端接口返回是否出错
          loading.value = false;
          listData.value = response.data.content.list;  // 显示内容

          // 重置分页按钮
          pagination.value.current = p.current;
          pagination.value.total = response.data.content.total;
        } else {
          message.error(response.data.message);
        }


      })
    }

    //-------------分页--------------
    /**
     * 分页的跳转页面处理
     * @param pagination
     */
    const handleTableChange = (pagination: any) => {
      // console.log("pagination:" + pagination);
      handleQuery({
        current: pagination.current,
        pageSize: pagination.pageSize,
      });
    };

    //-------------表单--------------
    const downloadList = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const categoryIds = ref();
    const categoryData = ref();

    const handleModalOk = () => {
      modalLoading.value = true;
      downloadList.value.categoryId1 = categoryIds.value[0];  // 保存之前先把两个分类从表单中提取出来
      downloadList.value.categoryId2 = categoryIds.value[1];

      axios.post("/downloadList/save", downloadList.value).then((response) => {
        // console.log(response);
        const data = response.data;
        modalLoading.value = false;


        if (data.success) {
          modalVisible.value = false;

          // 重新加载列表
          handleQuery({
            current: pagination.value.current,
            pageSize: pagination.value.pageSize,
          });
        } else {
          message.error(response.data.message);
        }
      })

    };

    /**
     * 表格的编辑按钮
     */
    const buttonEdit = (record: any) => {
      modalVisible.value = true;
      downloadList.value = Tool.copy(record);
      categoryIds.value = [downloadList.value.categoryId1, downloadList.value.categoryId2];  // 编辑时表单的分类显示需要再从 downloadList 中提取出来
    };

    /**
     * 表格的删除按钮
     */
    const buttonDelete = (id: number) => {
      axios.delete("/downloadList/delete/" + id).then((response) => {
        const data = response.data;

        if (data.success) {
          // 重新加载列表
          handleQuery({
            current: pagination.value.current,
            pageSize: pagination.value.pageSize,
          });
        }
      })
    };

    /**
     * 分类数据查询
     */
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/category/selectAll").then((response) => {
        loading.value = false;
        if (response.data.success) {  // 判断后端接口返回是否出错
          categoryData.value = Tool.array2Tree(response.data.content, 0);

        } else {
          message.error(response.data.message);
        }
      })
    }

    onMounted(() => {
      handleQueryCategory();
      handleQuery({
        current: pagination.value.current,
        pageSize: pagination.value.pageSize,
      });
    });

    return {
      loading,
      listData,
      pagination,
      columns,
      handleTableChange,

      buttonEdit,
      addDownloadItem,
      buttonDelete,

      downloadList,
      modalVisible,
      modalLoading,
      handleModalOk,

      onSearch,

      categoryIds,
      categoryData,
    };

  },
});
</script>

<style scoped>

</style>