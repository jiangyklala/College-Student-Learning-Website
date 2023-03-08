<template>
  <a-layout-content class="layout-content" id="layout-content">
    <span v-for="list in listData" v-bind:key="list">
      <div class="mainTags-div">
        <tag-two-tone/>
      </div>
      <a-list
          item-layout="vertical"
          size="middle"
          :data-source="list"
          :grid="{ gutter: 50, column: 2}"
      >

        <template #renderItem="{ item }">
          <div class="courseItem-div" >
            <a-list-item key="item.name" class="listItem">
            <a-list-item-meta :description="item.description">
              <template #title>
                <a class="titleA">{{ item.name }}</a>
              </template>
              <template #avatar>
                <a-avatar :src="item.avatarLink" class="item-avatar"/>
              </template>
            </a-list-item-meta>
          </a-list-item>
          </div>
      </template>

    </a-list>

    </span>

  </a-layout-content>
</template>

<script>

import {defineComponent, onMounted} from "vue";
import {Column} from "ant-design-vue/es/vc-table";

export default defineComponent({
  components: {
    Column,
  },
  name: "Column",
  setup() {

    const listData = [

    ]

    onMounted(() => {
      console.log("ad");
    });

    return {
      listData,
    };

  }
});
</script>

<style scoped>
.layout-content {
  padding: 20px 20px 30px 80px;
  width: 1200px;
  height: 800px;
  min-height: 200px;
  margin: 20px auto 100px;
  overflow: hidden;
  background: #fff;
}
</style>