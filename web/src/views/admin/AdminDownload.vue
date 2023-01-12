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
            <a-button type="link">
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
        dataIndex: 'category_id1',
        width: '10%',
      },
      {
        title: '分类二',
        dataIndex: 'category_id2',
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

    const handleTableChange = (pagination: any) => {
      // console.log("pagination:" + pagination);
      handleQuery({
        current: pagination.current,
        pageSize: pagination.pageSize,
      });
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
      handleTableChange
    };

  },
});
</script>

<style scoped>

</style>