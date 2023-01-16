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
        <a-button type="primary" @click="addCategoryItem">
          新增
        </a-button>
      </a-space>
    </div>
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
  <a-modal
      title="分类表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form
        :model="category"
        :label-col="{ span : 4 }"
    >
      <a-form-item label="名称">
        <a-input v-model:value="category.name"/>
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.parent"/>
      </a-form-item>
      <a-form-item label="排序">
        <a-input v-model:value="category.sort"/>
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
  name: "AdminCategory",
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
        width: '40%',
      },
      {
        title: '父分类',
        dataIndex: 'parent',
        width: '20%',
      },
      {
        title: '排序',
        dataIndex: 'sort',
        width: '20%',
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
    const addCategoryItem = () => {
      modalVisible.value = true;
      category.value = {};  // 清空当前的数据信息
    };

    /**
     * 数据查询
     * @param p
     */
    const handleQuery = (p: any) => {
      axios.get("/category/list", {
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
    const category = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;

      axios.post("/category/save", category.value).then((response) => {
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
      category.value = Tool.copy(record);
    };

    /**
     * 表格的删除按钮
     */
    const buttonDelete = (id: number) => {
      axios.delete("/category/delete/" + id).then((response) => {
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

    onMounted(() => {
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
      addCategoryItem,
      buttonDelete,

      category,
      modalVisible,
      modalLoading,
      handleModalOk,

      onSearch,
    };

  },
});
</script>

<style scoped>

</style>