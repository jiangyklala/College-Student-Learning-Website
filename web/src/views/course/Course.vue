<template>
  <a-layout-content class="layout-content" id="layout-content">
    <span v-for="list in listData" v-bind:key="list">
      <div class="mainTags-div">
        <tag-two-tone/>
        {{ getCategoryNameById(list[0].categoryId2) }}
      </div>
      <a-list
          item-layout="vertical"
          size="middle"
          :data-source="list"
          :loading="mainLoading"
          :grid="{ gutter: 50, column: 2}"
      >

        <template #renderItem="{ item }">
          <div class="courseItem-div" @click="courseItemOnClick(item)">
            <a-list-item key="item.name" class="listItem">
            <a-list-item-meta :description="item.description">
              <template #title>
                <a class="titleA">{{ item.name }}</a>
              </template>
              <template #avatar>
                <a-avatar :src="item.avatar" class="item-avatar"/>
              </template>
            </a-list-item-meta>
          </a-list-item>
          </div>
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
import router from "@/router";

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

          autoLayoutHeight();

        } else {
          message.error(response.data.message);
        }
      })
    }

    /**
     * 自动调节"课程页"布局高度
     */
    function autoLayoutHeight() {
      let tagsNum = listData.value.length;
      let courseRowNum = 0;
      for (let i = 0; i < listData.value.length; ++i) {
        courseRowNum += (listData.value[i].length + 1) / 2;
      }

      var lala = document.getElementById("layout-content");
      if (lala != null) lala.style.height = tagsNum * 80 + courseRowNum * 210 + 'px';
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

    /**
     * 点击课程跳转的播放界面
     * @param item
     */
    const courseItemOnClick = (item: any) => {
      console.log(item);
      const routeData = router.resolve({
        path: "/course/VideosPlayer",
      });

      sessionStorage.setItem("CourseItem", JSON.stringify(item));  // 临时存储

      window.open(routeData.href, '课程播放');
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
      courseItemOnClick,
    };


  },
})
</script>

<style scoped>

.layout-content {
  padding: 30px 150px;
  width: 1200px;
  min-height: 200px;
  margin: 20px auto 100px;
  overflow: hidden;
  background: #fff;
}

.mainTags-div {
  padding-top: 20px;
  width: 300px;
  height: 80px;
  font-weight: 700;
  font-size: 25px;
}

.ant-list-item-meta {
  display: flex;
  flex: 1;
  align-items: flex-start;
  max-width: 100%;
  width: 700px;
  background-color: rgb(244, 244, 244);
}

.item-avatar {
  width: 200px;
  height: 200px;
  padding-top: 10px;
  padding-left: 10px;
  padding-bottom: 10px;
  border-radius: 8%;
}

.titleA {
  padding-top: 10px;
  display: block;
  width: 100px !important;
}

</style>