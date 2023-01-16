<template>
  <a-layout-content style="padding: 0 250px">
    <div class="body">
      <a-space direction="horizontal" size="large">
        <a-button type="primary" @click="addCategoryItem">
          新增
        </a-button>
      </a-space>
    </div>
    <a-table
        :columns="columns"
        :data-source="tableData"
        :row-key="record => record.id"
        :loading="loading"
        :pagination="false"
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
    const tableData = ref();



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
     */
    const handleQuery = () => {
      axios.get("/category/selectAll").then((response) => {

        if (response.data.success) {  // 判断后端接口返回是否出错
          loading.value = false;
          // tableData.value = [];
          tableData.value = Tool.array2Tree(response.data.content, 0)
          listData.value = response.data.content;  // 显示内容

        } else {
          message.error(response.data.message);
        }


      })
    }



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
          handleQuery();
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
          handleQuery();
        }
      })
    };

    onMounted(() => {
      handleQuery();
    });

    return {
      loading,
      tableData,
      listData,
      columns,

      buttonEdit,
      addCategoryItem,
      buttonDelete,

      category,
      modalVisible,
      modalLoading,
      handleModalOk,

    };

  },
});
</script>

<style scoped>

</style>