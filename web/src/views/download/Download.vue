<template>
  <a-layout-content style="padding: 0 250px">
    <a-list
        item-layout="horizontal"
        :data-source="listData"
        :pagination="pagination"
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
    </a-list>
  </a-layout-content>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import {EyeTwoTone, ThunderboltTwoTone, DownloadOutlined} from '@ant-design/icons-vue';
import axios from 'axios';

export default defineComponent({
  components: {
    EyeTwoTone,
    ThunderboltTwoTone,
    DownloadOutlined,
  },
  name: 'Home',
  setup() {
    // console.log("setup");
    // const downloadList = ref();
    const listData = ref();

    const pagination = {
      // onChange: (page: number) => {
      //   console.log(page);
      // },
      pageSize: 3,
    };

    const actions: Record<string, string>[] = [

      {type: 'EyeTwoTone', text: '156'},
      {type: 'ThunderboltTwoTone', text: '355'},
    ];

    onMounted(() => {
      axios.get("/downloadList/list").then(
          (response) => {
            const data = response.data;
            listData.value = data.content;
            console.log(response);
          }
      )
    });

    return {
      listData,
      pagination,
      actions,
    };
  },
});
</script>

<style scoped>
</style>
