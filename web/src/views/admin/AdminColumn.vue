<template>
  <a-layout-content class="layout-content">
    <div>
      <a-space direction="horizontal" size="large">
        <a-input-search
            placeholder="输入待搜索名称"
            enter-button="Search"
            size="large"
            @search="onSearch"
            class="input-search"
        />
        <a-button type="primary"
                  @click="addDownloadItem"
                  class="new-column-btn"
                  size="large"
        >
          新增
        </a-button>
      </a-space>
    </div>

    <!--    专栏管理表格-->
    <a-table
        :columns="columns"
        :data-source="listData"
        :row-key="record => record.id"
        :pagination="pagination" @change="handleTableChange"
        :loading="loading"
        bordered
        class="column-table"
    >
      <template v-slot:category="{ text, record }">
        <span>{{ getCategoryNameById(record.categoryId1) }} / {{ getCategoryNameById(record.categoryId2) }}</span>
      </template>
      <template v-slot:bodyCell="{ column, record, index }">
        <template v-if="column.dataIndex === 'action'">
          <a-space size="small">
            <a-button type="link" @click="btnToAdminDoc(record)">
              文档管理
            </a-button>
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
      title="专栏表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form
        :model="columnList"
        :label-col="{ span : 4 }"
    >
      <a-form-item label="名称">
        <a-input v-model:value="columnList.name"/>
      </a-form-item>
      <a-form-item label="分类">
        <!--        级联选择-->
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="categoryTree"/>
      </a-form-item>
      <a-form-item label="专栏链接">
        <a-textarea v-model:value="columnList.downloadLink"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import {message} from 'ant-design-vue';
import axios from 'axios';
import {Tool} from "@/utils/tool";
import router from "@/router";

export default defineComponent({
  components: {},
  name: "AdminColumn",
  setup: function () {



    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        width: '20%',
      },
      {
        title: '分类',
        slots: {customRender: 'category'},
        width: '40%',
      },
      {
        title: '文档数',
        dataIndex: 'docCount',
        width: '10%',
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount',
        width: '10%',
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount',
        width: '15%',
      },
      {
        title: '操作',
        dataIndex: 'action',
        width: '30%',
      },
    ];


    //-------------页面--------------

    const loading = ref(true);
    const listData = ref();

    /**
     * 专栏列表数据查询
     * @param p
     */
    const columnListALlQuery = (p: any) => {
      axios.get("/columnList/selectAll", {
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


    /**
     * 分类数据查询
     */
    let categorys: any;
    const handleQueryCategory = () => {
      axios.get("/category/selectAllOBSort").then((response) => {
        if (response.data.success) {  // 判断后端接口返回是否出错
          categorys = response.data.content;
          categoryTree.value = Tool.array2Tree(response.data.content, 0);

          columnListALlQuery({   // 专栏列表的显示需要用到分类的信息, 由于 axios 是异步的, 所以必须在分类查询完成后再进行专栏列表的查询显示
            current: pagination.value.current,
            pageSize: pagination.value.pageSize,
          });
        } else {
          message.error(response.data.message);
        }
      })
    }


    //-------------表格--------------

    /**
     * 新增按钮
     * 注: 这里不需要写具体的新增逻辑, 已经在对话框的"确认"按钮的逻辑中写过了
     */
    const addDownloadItem = () => {
      modalVisible.value = true;
      columnList.value = {};  // 清空当前的数据信息
    };

    /**
     * 表格的编辑按钮
     */
    const buttonEdit = (record: any) => {
      modalVisible.value = true;
      columnList.value = Tool.copy(record);
      categoryIds.value = [columnList.value.categoryId1, columnList.value.categoryId2];  // 编辑时表单的分类显示需要再从 columnList 中提取出来
    };

    /**
     * 表格的删除按钮
     */
    const buttonDelete = (id: number) => {
      axios.delete("/columnList/delete/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          // 重新加载列表
          columnListALlQuery({
            current: pagination.value.current,
            pageSize: pagination.value.pageSize,
          });
        }
      })
    };

    /**
     * "管理文档"跳转到文档管理页面
     */
    const btnToAdminDoc = (item: any) => {
      const routeData = router.resolve({
        path: "/admin/AdminDoc",
      });
      // console.log(item);

      sessionStorage.setItem("ColumnId", item.id);                   // 临时存储

      window.open(routeData.href, '课程播放');
    }


    //-------------表单--------------
    const columnList = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const categoryIds = ref();
    const categoryTree = ref();

    /**
     * 表单确认按钮
     */
    const handleModalOk = () => {
      modalLoading.value = true;
      columnList.value.categoryId1 = categoryIds.value[0];  // 保存之前先把两个分类从表单中提取出来
      columnList.value.categoryId2 = categoryIds.value[1];

      axios.post("/columnList/save", columnList.value).then((response) => {
        // console.log(response);
        const data = response.data;
        modalLoading.value = false;


        if (data.success) {
          modalVisible.value = false;

          // 重新加载列表
          columnListALlQuery({
            current: pagination.value.current,
            pageSize: pagination.value.pageSize,
          });
        } else {
          message.error(response.data.message);
        }
      })

    };


    //-------------分页--------------

    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });

    /**
     * 分页的跳转页面处理
     * @param pagination
     */
    const handleTableChange = (pagination: any) => {
      // console.log("pagination:" + pagination);
      columnListALlQuery({
        current: pagination.current,
        pageSize: pagination.pageSize,
      });
    };


    //-------------搜索框--------------
    const onSearch = (searchValue: string) => {
      columnListALlQuery({
        current: 1,
        pageSize: pagination.value.pageSize,
        name: searchValue,
      })
    };

    //-------------其它--------------

    /**
     * 根据目录id返回具体的分类名称
     */
    const getCategoryNameById = (cid: number) => {
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          // 这里直接 return item.name 不起作用
          result = item.name;
        }
      });
      return result;
    }

    onMounted(() => {
      handleQueryCategory();

    });

    return {
      loading,
      listData,
      pagination,
      columns,
      handleTableChange,
      getCategoryNameById,

      buttonEdit,
      addDownloadItem,
      buttonDelete,
      btnToAdminDoc,

      columnList,
      modalVisible,
      modalLoading,
      handleModalOk,

      onSearch,

      categoryIds,
      categoryTree,
    };

  },
});
</script>

<style scoped>

.layout-content {
  padding: 30px 80px;
  width: 1000px;
  height: 800px;
  min-height: 200px;
  margin: 20px auto 100px;
  overflow: hidden;
  background: rgb(237,239,242);
}

.column-table {
  padding-top: 15px;
}

</style>