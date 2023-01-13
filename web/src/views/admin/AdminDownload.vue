<template>
  <a-layout-content style="padding: 0 250px">
    <div class="title">
      <h1>下载列表管理</h1>
    </div>
    <a-table
        :columns="columns"
        :data-source="listData"
        :row-key="record => record.id"
        :pagination="pagination" @change="handleTableChange"
        :loading="Loading"
        bordered>
      <template v-slot:bodyCell="{ column, record, index }">
        <template v-if="column.dataIndex === 'action'">
          <a-space size="small">
            <a-button type="link" @click="buttonEdit(record)">
              编辑
            </a-button>
            <a-button type="link">
              删除
            </a-button>
          </a-space>
        </template>
      </template>
    </a-table>
  </a-layout-content>
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
      <a-form-item label="分类一">
        <a-input v-model:value="downloadList.categoryId1"/>
      </a-form-item>
      <a-form-item label="分类二">
        <a-input v-model:value="downloadList.categoryId2"/>
      </a-form-item>
      <a-form-item label="描述" type="text">
        <a-input v-model:value="downloadList.description"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import axios from 'axios';

export default defineComponent({
  components: {},
  name: "AdminDownload",
  setup() {
    const listData = ref();
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    });

    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        width: '10%',
      },
      {
        title: '描述',
        dataIndex: 'description',
        width: '65%',
      },
      {
        title: '分类一',
        dataIndex: 'categoryId1',
        width: '10%',
      },
      {
        title: '分类二',
        dataIndex: 'categoryId2',
        width: '10%',
      },
      {
        title: '大小',
        dataIndex: 'size',
        width: '10%',
      },
      {
        title: '操作',
        dataIndex: 'action',
      },
    ];

    /**
     * 数据查询
     * @param p
     */
    const handleQuery = (p: any) => {
      loading.value = true;
      axios.get("/downloadList/list", {
        params: {
          page: p.current,
          size: p.pageSize,
        }
      }).then((response) => {
        loading.value = false;
        listData.value = response.data.content.list;

        // 重置分页按钮
        pagination.value.current = p.current;
        pagination.value.total = response.data.content.total;
      })
    }

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
    const downloadList = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;

      axios.post("/downloadList/save", downloadList.value).then((response) => {
        console.log(response);
        const data = response.data;

        if (data.success) {
          modalVisible.value = false;
          modalLoading.value = false;

          // 重新加载列表
          handleQuery({
            current: pagination.value.current,
            pageSize: pagination.value.pageSize,
          });
        }
      })

    };

    /**
     * 表格的编辑按钮
     */
    const buttonEdit = (record: any) => {
      modalVisible.value = true;
      downloadList.value = record;
    };

    onMounted(() => {
      handleQuery({
        current: pagination.value.current,
        pageSize: pagination.value.pageSize,
      });
    });

    return {
      listData,
      pagination,
      columns,
      loading,
      handleTableChange,

      buttonEdit,
      modalVisible,
      modalLoading,
      handleModalOk,

      downloadList,
    };

  },
});
</script>

<style scoped>

</style>