<template>
  <a-layout-content class="layout-content">

    <!--    分类导航显示-->
    <a-menu
        mode="horizontal"
        @click="handleMeunClick"
    >
      <a-menu-item :key="-1">
        <router-link to="/download/Download">
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

    <!--    下载列表-->
    <a-list
        item-layout="horizontal"
        :data-source="listData"
        :loading="loading"
    >
      <template #renderItem="{ item }">
        <a-list-item class="each-item" key="item.name">

          <a-list-item-meta>
            <template #title>
              <a :href="item.href">{{ item.name }}</a>
            </template>
          </a-list-item-meta>

          <span class="itemCategorySpan">
            {{ getCategoryNameById(item.categoryId1) }} / {{ getCategoryNameById(item.categoryId2) }}
          </span>

          <template #actions>
            <span class="thunderbolt">
              <thunderbolt-two-tone/>
              {{ item.downloadCount }}
            </span>
            <a-button
                class="download-button"
                type="primary"
                shape="round"
                v-bind:href="item.downloadLink"
                @click="downloadBtnClick(item)"
            >
              <template #icon>
                <DownloadOutlined/>
              </template>
            </a-button>
              <a-button type="primary" @click="btn(item)">Primary Button</a-button>
          </template>
        </a-list-item>

      </template>

      <a-pagination
          class="pagination"
          v-model:current="pagination.current"
          v-model:pageSize="pagination.pageSize"
          :total="pagination.total"
          @change="paginationChange"
      />

    </a-list>
  </a-layout-content>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import {DownloadOutlined, EyeTwoTone, ThunderboltTwoTone} from '@ant-design/icons-vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/utils/tool";
import router from '@/router';


export default defineComponent({
  components: {
    EyeTwoTone,
    ThunderboltTwoTone,
    DownloadOutlined,
  },
  name: 'Download',
  setup() {
    const downloadModalVis = ref(false);
    const mainLoading = ref(true);
    const listData = ref();
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0,
    });

    const actions: Record<string, string>[] = [
      {type: 'ThunderboltTwoTone', text: 'item.downloadCount'},
    ];

    //-------------数据查询--------------

    const handleQuery = (p: any) => {
      axios.get("/downloadList/list", {
        params: {
          page: p.current,
          size: p.pageSize,
        }
      }).then((response) => {

        if (response.data.success) {  // 判断后端接口返回是否出错
          mainLoading.value = false;
          listData.value = response.data.content.list;

          // 重置分页按钮
          pagination.value.current = p.current;
          pagination.value.total = response.data.content.total;
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

        } else {
          message.error(response.data.message);
        }
      })
    }

    /**
     * 根据目录id查询下载项
     * @param p
     */
    const selectByCategory = (p: any) => {
      mainLoading.value = true;
      axios.get("/downloadList/selectByCategoryId", {
        params: {
          page: p.current,
          size: pagination.value.pageSize,
          categoryId: p.curCategoryItem.key,
        }
      }).then((response) => {
        if (response.data.success) {
          mainLoading.value = false;
          listData.value = response.data.content.list;
          console.log("response.data.content.total:" + response.data.content.total);

          // 重置分页按钮
          pagination.value.current = p.current;
          pagination.value.total = response.data.content.total;
        } else {
          message.error(response.data.message);
        }
      })
    }

    /**
     * 根据目录id返回具体的分类名称
     **/
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

    //-------------按钮--------------
    const downloadModalOK = () => {
      downloadModalVis.value = false;
    }

    /**
     * 分类导航栏点击
     * @param item
     */
    let curCategoryItem: any;  // 分页模块需要使用当前选择的导航分类
    const handleMeunClick = (item: any) => {
      paginationNum = 1;
      curCategoryItem = item;
      selectByCategory({
        curCategoryItem: curCategoryItem,
        current: 1,
        pageSize: pagination.value.pageSize,
      });
    }

    /**
     * 点击资料的下载按钮
     * @param item
     */
    const downloadBtnClick = (item: any) => {
      mainLoading.value = true;
      ++item.downloadCount;
      console.log(item);
      axios.post("/downloadList/save", item).then((response) => {

        const data = response.data;

        if (data.success) {
          mainLoading.value = false;
          // 重新加载列表
          handleQuery({
            current: pagination.value.current,
            pageSize: pagination.value.pageSize,
          });
        } else {
          message.error(response.data.message);
        }
      })
    }

    function btn(item: any) {
      console.log(item);
      const routeData = router.resolve({
        path: "/course/VideosPlayer",
        query: {
          id: item.id,
        }
      });

      window.open(routeData.href, '_blank');
    }


    //-------------分页--------------

    // 分页选择器
    let paginationNum = 0;
    const paginationChange = (current: any) => {
      if (paginationNum === 0) {
        handleListChange(current);
      } else if (paginationNum === 1) {
        handleCategoryChange(current);
      }
    }

    /**
     * 对"全部"进行分页
     * @param current
     */
    const handleListChange = (current: any) => {
      // console.log("pagination:" + current);
      handleQuery({
        current: current,
        pageSize: pagination.value.pageSize,
      });
    };

    /**
     * 对某个分类下的数据进行分页
     * @param current.item 当前所选的分类
     */
    const handleCategoryChange = (current: any) => {
      // console.log("pagination:" + current);
      selectByCategory({
        curCategoryItem: curCategoryItem,
        current: current,
        pageSize: pagination.value.pageSize,
      })
    };

    onMounted(() => {
      handleQueryCategory();
      handleQuery({
        current: pagination.value.current,
        pageSize: pagination.value.pageSize,
      });
    });

    return {
      loading: mainLoading,
      listData,
      pagination,
      actions,
      paginationChange,

      downloadModalOK,
      downloadModalVis,
      downloadBtnClick,
      getCategoryNameById,
      btn,

      categoryTree,
      handleMeunClick,
    };
  },


});

</script>

<style scoped>

.each-item {
  background: white;
  padding: 5px;
  margin-top: 5px;
  margin-bottom: 5px;
}

.pagination {
  text-align: center;
}

.layout-content {
  padding: 10px 250px;
}

.thunderbolt {
  display: block;
  width: 50px;
  text-align: left;
}

.itemCategorySpan {
  display: block;
  width: 200px;
  text-align: left;
}


</style>
