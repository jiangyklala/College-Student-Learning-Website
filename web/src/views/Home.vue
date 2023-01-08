<template>
  <a-layout-content style="padding: 0 50px">
    <a-list
        item-layout="horizontal"
        :data-source="listData"
        :pagination="pagination"
    >
      <template #renderItem="{ item }">
        <a-list-item key="item.title">
          <template #actions>
            <span v-for="{ type, text } in actions" :key="type">
            <component :is="type" style="margin-right: 8px"/>
            {{ text }}
            </span>
            <a-button type="primary" shape="round" :size="size">
              <template #icon>
                <DownloadOutlined />
              </template>
            </a-button>
          </template>
          <a-list-item-meta >
            <template #title>
              <a :href="item.href">{{ item.title }}</a>
            </template>
          </a-list-item-meta>
          {{ item.description }}
        </a-list-item>
      </template>
    </a-list>
  </a-layout-content>
</template>

<script lang="ts">
import {defineComponent, onMounted} from 'vue';
import {EyeTwoTone, ThunderboltTwoTone, DownloadOutlined} from '@ant-design/icons-vue';

const listData : Record<string, string>[] = [];

for (let i = 0; i < 23; i++) {
  listData.push({
    href: 'https://www.antdv.com',
    title: `ant design vue part ${i}`,
    description:
      'soifjasoifj osjf oijasifjosajfoi ajsojo',
  });
}

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

    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 10,
    };
    const actions: Record<string, string>[] = [

      { type: 'EyeTwoTone', text: '156' },
      { type: 'ThunderboltTwoTone', text: '355' },
    ];

    onMounted(() => {
      // console.log("onMounted");
      // axios.get("http://localhost:8880/downloadList/list?name=资料2").then(
      //     (response) => {
      //       const data = response.data;
      //       downloadList.value = data.content;
      //       console.log(response);
      //     }
      // )
    });



    return {
      // downloadList,listData,
      pagination,
      actions,
      listData,
    };
  },
});
</script>

<style scoped>
</style>
