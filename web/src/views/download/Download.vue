<template>
  <a-layout-content style="padding: 0 250px">
    <a-list
        item-layout="horizontal"
        :data-source="listData"
    >
      <template #renderItem="{ item }">
        <a-list-item key="item.name">
          <template #actions>
            <span v-for="{ type, text } in actions" :key="type">
            <component :is="type" style="margin-right: 8px"/>
            {{ text }}
            </span>
            <a-button type="primary" shape="round">
              <template #icon>
                <DownloadOutlined/>
              </template>
            </a-button>
          </template>
          <a-list-item-meta>
            <template #title>
              <a :href="item.href">{{ item.name }}</a>
            </template>
          </a-list-item-meta>
          {{ item.description }}
        </a-list-item>
      </template>
      <a-pagination
          v-model:current="pagination.current"
          v-model:pageSize="pagination.pageSize"
          :total="pagination.total"
          @change="handleListChange"/>
    </a-list>
  </a-layout-content>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import {DownloadOutlined, EyeTwoTone, ThunderboltTwoTone} from '@ant-design/icons-vue';
import axios from 'axios';

export default defineComponent({
  components: {
    EyeTwoTone,
    ThunderboltTwoTone,
    DownloadOutlined,
  },
  name: 'Download',
  setup() {
    const listData = ref();
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0,
    });

    const actions: Record<string, string>[] = [
      {type: 'EyeTwoTone', text: '156'},
      {type: 'ThunderboltTwoTone', text: '355'},
    ];

    const handleQuery = (p: any) => {
      axios.get("/downloadList/list", {
        params: {
          page: p.current,
          size: p.pageSize,
        }
      }).then((response) => {
        listData.value = response.data.content.list;
        // console.log(response);

        pagination.value.current = p.current;
        pagination.value.total = response.data.content.total;
      })
    }

    const handleListChange = (current: any) => {
      console.log("pagination:" + current);
      handleQuery({
        current: current,
        pageSize: pagination.value.pageSize,
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
      actions,
      handleListChange,
    };
  },
});
</script>

<style scoped>
</style>
