<template>
  <a-layout-content class="layout-content">
    <span v-for="list in listData" v-bind:key="list">
      <div class="mainTagsDiv">
        <tag-two-tone/>
        {{ getCategoryNameById(list[0].categoryId2) }}
      </div>
      <a-list
          item-layout="vertical"
          size="middle"
          :data-source="list"
          :loading="mainLoading"
          :grid="{ gutter: 50, column: 3}"
      >

        <template #renderItem="{ item }">
        <a-list-item key="item.name">
          <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component :is="type" style="margin-right: 8px"/>
            {{ text }}
          </span>
          </template>
          <a-list-item-meta :description="item.description">
            <template #title>
              <a :href="item.id">{{ item.name }}</a>
            </template>
            <template #avatar>
              <a-avatar :src="item.avatar"/>
            </template>
          </a-list-item-meta>
          {{ item.size }}
        </a-list-item>
      </template>

    </a-list>

    </span>

  </a-layout-content>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import {message} from "ant-design-vue";
import axios from "axios";
import {Tool} from "@/utils/tool";

const listData = [];

export default defineComponent({
  components: {},
  name: 'Course',
  setup() {
    const mainLoading = ref();
    const listData = ref();
    const pagination = ref({
      current: 1,
      pageSize: 300,
      total: 0,
    });


    //-------------数据查询--------------

    const handleQuery = () => {
      axios.get("/courseList/selectAllGpByCgId2", {}).then((response) => {

        if (response.data.success) {
          mainLoading.value = false;
          listData.value = response.data.content;

          console.log(listData);

        } else {
          message.error(response.data.message);
        }
      })
    }

    /**
     * 分类数据查询
     */
    let categorys: any;
    const categoryTree = ref();

    const handleQueryCategory = () => {
      axios.get("/category/selectAll").then((response) => {
        // loading.value = false;
        if (response.data.success) {  // 判断后端接口返回是否出错
          categorys = response.data.content;
          categoryTree.value = Tool.array2Tree(response.data.content, 0);

          handleQuery();

        } else {
          message.error(response.data.message);
        }
      })
    }

    const paginationChange = (current: any) => {
      // console.log("pagination:" + current);
      handleQuery();
    };

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
      listData,
      pagination,
      mainLoading,
      // actions,

      paginationChange,
      getCategoryNameById,
    };


  },
})
</script>

<style scoped>

.layout-content {
  padding: 30px 150px;
  width: 1200px;
  height: 1200px;
  min-height: 200px;
  margin: 20px auto 100px;
  overflow: hidden;
  background: #fff;
}

.mainTagsDiv {
  width: 300px;
  height: 70px;
  font-weight: 700;
  font-size: 25px;
}


</style>