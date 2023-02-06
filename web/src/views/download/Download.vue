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

          <template #actions>
            <span v-for="{ type, text } in actions" :key="type">
              <component :is="type" style="margin-right: 8px"/>
                {{ text }}
            </span>
            <a-button class="download-button" type="primary" shape="round">
              <template #icon>
                <DownloadOutlined/>
              </template>
            </a-button>
          </template>
        </a-list-item>

        <a-modal v-model:visible="downloadModalVis" @ok="downloadModalOK">
          <p>Some contents...</p>
          <p>Some contents...</p>
          <p>Some contents...</p>
        </a-modal>

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

export default defineComponent({
  components: {
    EyeTwoTone,
    ThunderboltTwoTone,
    DownloadOutlined,
  },
  name: 'Download',
  setup() {
    const downloadModalVis = ref(false);
    const loading = ref(true);
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

    //-------------数据查询--------------

    const handleQuery = (p: any) => {
      axios.get("/downloadList/list", {
        params: {
          page: p.current,
          size: p.pageSize,
        }
      }).then((response) => {

        if (response.data.success) {  // 判断后端接口返回是否出错
          loading.value = false;
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
      loading.value = true;
      axios.get("/downloadList/selectByCategoryId", {
        params: {
          page: pagination.value.current,
          size: pagination.value.pageSize,
          categoryId: p.item.key,
        }
      }).then((response) => {
        if (response.data.success) {
          loading.value = false;
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

    //-------------按钮--------------
    const downloadModalOK = () => {
      downloadModalVis.value = false;
    }

    /**
     * 分类导航栏点击
     * @param item
     */
    let meunItem: any;
    const handleMeunClick = (item: any) => {
      paginationNum = 1;
      meunItem = item;
      selectByCategory({
        item: meunItem,
        current: 1,
        pageSize: pagination.value.pageSize,
      });
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

    const handleListChange = (current: any) => {
      // console.log("pagination:" + current);
      handleQuery({
        current: current,
        pageSize: pagination.value.pageSize,
      });
    };

    const handleCategoryChange = (current: any) => {
      // console.log("pagination:" + current);
      selectByCategory({
        item: meunItem,
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
      loading,
      listData,
      pagination,
      actions,
      paginationChange,

      downloadModalOK,
      downloadModalVis,

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


</style>
