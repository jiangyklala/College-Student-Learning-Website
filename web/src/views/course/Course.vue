<template>
  <a-layout-content class="layout-content">
    <a-list
        item-layout="vertical"
        size="large"
        :data-source="listData"
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
      pageSize: 3,
      total: 0,
    });


    //-------------数据查询--------------

    const handleQuery = (p: any) => {
      axios.get("/courseList/list", {
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

    const paginationChange = (current: any) => {
      // console.log("pagination:" + current);
      handleQuery({
        current: current,
        pageSize: pagination.value.pageSize,
      });
    };

    onMounted(() => {
      handleQueryCategory();
      handleQuery({
        current: pagination.value.current,
        pageSize: pagination.value.pageSize,
      });
    });

    return {
      listData,
      pagination,
      mainLoading,
      // actions,

      paginationChange,
    };


  },
})
</script>

<style scoped>

.layout-content {
  padding: 30px 150px;
}

</style>