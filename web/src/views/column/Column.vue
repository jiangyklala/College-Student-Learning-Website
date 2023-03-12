<template>
  <a-layout-content class="layout-content" id="layout-content">

    <!--    分类导航显示-->
    <a-menu
        mode="horizontal"
        @click="handleMeunClick"
        class="category-menu"
    >
      <a-menu-item :key="-1">
        <router-link to="/column/Column">
          全部
        </router-link>
      </a-menu-item>
      <a-sub-menu v-for="item in categoryTree" :key="item.id">
        <template v-slot:title>
          <span>{{ item.name }}</span>
        </template>
        <a-menu-item v-for="child in item.children" :key="child.id">
          <span>{{ child.name }}</span>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
    <a-list
          item-layout="vertical"
          size="middle"
          :data-source="listData"
          :grid="{ gutter: [80, 30], column: 3}"
          :loading="mainLoading"
          class="column-list"
    >
        <template #renderItem="{ item }">
          <div class="column-div" @click="columnItemOnClick(item)">
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


  </a-layout-content>
</template>

<script lang="ts">

import {defineComponent, onMounted, ref} from "vue";
import {Column} from "ant-design-vue/es/vc-table";
import {message} from "ant-design-vue";
import axios from "axios";
import router from "@/router";
import {Tool} from "@/utils/tool";

export default defineComponent({
  components: {
    Column,
  },
  name: "Column",
  setup() {

    //-------------页面--------------

    const mainLoading = ref();
    const listData = ref();

    /**
     * 专栏列表查询
     */
    const columnAllQuery = () => {
      axios.get("/columnList/selectAll", {
        params: {
          page: 1,
          size: 100,
        }
      }).then((response) => {

        if (response.data.success) {
          mainLoading.value = false;
          listData.value = response.data.content.list;

          console.log(listData);

          // autoLayoutHeight();   // 自动调整布局

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
      axios.get("/category/selectAllOBSort").then((response) => {
        // loading.value = false;
        if (response.data.success) {  // 判断后端接口返回是否出错
          categorys = response.data.content;
          categoryTree.value = Tool.array2Tree(response.data.content, 0);

          columnAllQuery();

        } else {
          message.error(response.data.message);
        }
      })
    }



    //-------------分类导航--------------
    /**
     * 分类导航栏点击
     * @param item
     */
    const handleMeunClick = (item: any) => {
      selectByCategory({
        categoryId2: item.key,
      });
    }

    //-------------专栏点击--------------

    /**
     * 点击专栏跳转的播放界面
     * @param item
     */
    const columnItemOnClick = (item: any) => {
      // console.log(item);
      const routeData = router.resolve({
        path: "/column/Doc",
      });

      sessionStorage.setItem("ColumnItemId", item.id);                   // 临时存储

      window.open(routeData.href, '专栏阅读');
    }


    //-------------其它--------------

    /**
     * 自动调节"专栏页"布局高度
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
     * 根据 categoryId2 查询在统一分类下的下载项
     * @param p
     */
    const selectByCategory = (p: any) => {
      mainLoading.value = true;
      axios.get("/columnList/selectByCategoryId2", {
        params: {
          categoryId2: p.categoryId2,
        }
      }).then((response) => {
        if (response.data.success) {
          mainLoading.value = false;
          listData.value = response.data.content;

        } else {
          message.error(response.data.message);
        }
      })
    }


    onMounted(() => {
      handleQueryCategory();
    });

    return {
      listData,
      mainLoading,
      categoryTree,
      // actions,

      getCategoryNameById,
      columnItemOnClick,
      handleMeunClick,
    };


  },
})
</script>

<style scoped>
.layout-content {
  padding: 20px 20px 30px 80px;
  width: 1200px;
  height: 100%;
  min-height: 1000px;
  margin: 20px auto 100px;
  overflow: hidden;
  background: #fff;
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
  width: 100px;
  height: 100px;
  padding-top: 3px;
  padding-left: 3px;
  padding-bottom: 3px;
  border-radius: 8%;
}

.column-list {
  padding-top: 30px;
}


</style>