<template>
  <a-layout-content style="padding: 0 250px" class="layout-content">
    <div class="body">
      <a-space direction="horizontal" size="large">
        <div class="course-list-info">
          课程管理
        </div>
        <a-input-search
            placeholder="输入待搜索 [课程] 名称"
            enter-button="Search"
            size="large"
            @search="onSearch"
        />
        <a-button type="primary" size="large" @click="btnAddCourse">
          新增
        </a-button>
      </a-space>
    </div>

    <!--    [课程] 管理表格-->
    <a-table
        :columns="courseListColumns"
        :data-source="courseList"
        :row-key="record => record.id"
        :pagination="courseListPagination" @change="courseTableChange"
        :loading="courseListLoading"
        bordered
        class="course-list-table"
    >
      <template v-slot:category="{ text, record }">
        <span>{{ getCategoryNameById(record.categoryId1) }} / {{ getCategoryNameById(record.categoryId2) }}</span>
      </template>
      <template v-slot:bodyCell="{ column, record, index }">
        <template v-if="column.dataIndex === 'action'">
          <a-space size="small">
            <a-button type="link" @click="btnEdit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="确认删除吗"
                ok-text="确认"
                cancel-text="取消"
                @confirm="btnDeleteCourse(record.id)"
            >
              <a-button type="link">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <div class="body">
      <a-space direction="horizontal" size="large">
        <div class="course-list-info">
          视频管理
        </div>
        <a-input-search
            placeholder="输入待搜索 [视频] 名称"
            enter-button="Search"
            size="large"
            @search="onSearch"
        />
        <a-button type="primary" size="large" @click="btnAddItem">
          新增
        </a-button>
      </a-space>
    </div>

    <!--    [视频] 管理表格-->
    <a-table
        :columns="courseItemColumns"
        :data-source="courseItems"
        :row-key="record => record.id"
        :pagination="courseItemPagination" @change="itemTableChange"
        :loading="courseListLoading"
        bordered
        class="course-item-table"
    >
      <template v-slot:bodyCell="{ column, record, index }">
        <template v-if="column.dataIndex === 'action'">
          <a-space size="small">
            <a-button type="link" @click="btnEditItem(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="确认删除吗"
                ok-text="确认"
                cancel-text="取消"
                @confirm="btnDeleteItem(record.id)"
            >
              <a-button type="link">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>
  </a-layout-content>

  <!--  操作弹窗-->
  <a-modal
      title="课程表单"
      v-model:visible="editModalVisible"
      :confirm-loading="editModalLoading"
      @ok="editModalOK"
  >
    <a-form
        :model="courseInModal"
        :label-col="{ span : 4 }"
    >
      <a-form-item label="课程名">
        <a-input v-model:value="courseInModal.name"/>
      </a-form-item>
      <a-form-item label="分类">
        <!--        级联选择-->
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="categoryTree"/>
      </a-form-item>
      <a-form-item label="视频封面">
        <a-input v-model:value="courseInModal.avatarLink"/>
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="courseInModal.description"/>
      </a-form-item>
    </a-form>
  </a-modal>

  <a-modal
      title="添加视频表单"
      v-model:visible="editItemModalVisible"
      :confirm-loading="editItemModalLoading"
      @ok="editItemModalOK"
  >
    <a-form
        :model="courseItemInModal"
        :label-col="{ span : 3 }"
    >
      <a-form-item label="所属课程名">
        <a-input v-model:value="courseItemInModal.course"/>
      </a-form-item>
      <a-form-item label="排序">
        <a-input v-model:value="courseItemInModal.sort"/>
      </a-form-item>
      <a-form-item label="视频连接">
        <a-input v-model:value="courseItemInModal.videoLink"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import {message} from 'ant-design-vue';
import axios from 'axios';
import {Tool} from "@/utils/tool";

export default defineComponent({
  components: {},
  name: "AdminCourse",
  setup: function () {
    const courseListLoading = ref(true);
    const courseList = ref();
    const courseListPagination = ref({
      current: 1,
      pageSize: 5,
      total: 0
    });

    const courseListColumns = [
      {
        title: '名称',
        dataIndex: 'name',
        width: '20%',
      },
      {
        title: '分类',
        slots: {customRender: 'category'},
        width: '40%',
      },
      {
        title: '点击量',
        dataIndex: 'clickCount',
        width: '15%',
      },
      {
        title: '操作',
        dataIndex: 'action',
        width: '30%',
      },
    ];

    const courseItemColumns = [
      {
        title: '所属课程',
        dataIndex: 'course',
        width: '30%',
      },
      {
        title: '排序',
        dataIndex: 'sort',
        width: '20%',
      },
      {
        title: '操作',
        dataIndex: 'action',
        width: '30%',
      },
    ];


    //-------------搜索框--------------]
    const onSearch = (searchValue: string) => {
      handleQuery({
        current: 1,
        pageSize: courseListPagination.value.pageSize,
        name: searchValue,
      })
    };

    //-------------页面--------------


    /**
     * 课程列表数据查询
     * @param p
     */
    const handleQuery = (p: any) => {
      axios.get("/courseList/list", {
        params: {
          page: p.current,
          size: p.pageSize,
          name: p.name,
        }
      }).then((response) => {

        if (response.data.success) {  // 判断后端接口返回是否出错
          courseListLoading.value = false;
          courseList.value = response.data.content.list;  // 显示内容

          // 重置分页按钮
          courseListPagination.value.current = p.current;
          courseListPagination.value.total = response.data.content.total;
        } else {
          message.error(response.data.message);
        }
      })
    }


    /**
     * 课程视频查询
     * @param p
     */
    const courseItems = ref();
    const courseItemLoading = ref(true);
    const courseItemPagination = ref({
      current: 1,
      pageSize: 5,
      total: 0
    });

    const courseItemQuery = (p: any) => {
      axios.get("/courseItem/select", {
        params: {
          page: p.current,
          size: p.pageSize,
          name: p.name,
        }
      }).then((response) => {

        if (response.data.success) {  // 判断后端接口返回是否出错
          courseItemLoading.value = false;
          courseItems.value = response.data.content.list;  // 显示内容

          // 重置分页按钮
          courseItemPagination.value.current = p.current;
          courseItemPagination.value.total = response.data.content.total;
        } else {
          message.error(response.data.message);
        }
      })
    }

    //-------------分页--------------
    /**
     * 课程分页的跳转页面处理
     */
    const courseTableChange = (pagination: any) => {
      // console.log("pagination:" + pagination);
      handleQuery({
        current: pagination.current,
        pageSize: pagination.pageSize,
      });
    };

    /**
     * 视频分页的跳转页面处理
     */
    const itemTableChange = (pagination: any) => {
      // console.log("pagination:" + pagination);
      courseItemQuery({
        current: pagination.current,
        pageSize: pagination.pageSize,
      });
    };

    //-------------课程表单--------------
    const courseInModal = ref();
    const editModalVisible = ref(false);
    const editModalLoading = ref(false);
    const categoryIds = ref();
    const categoryTree = ref();

    /**
     * 课程表单确认按钮
     */
    const editModalOK = () => {
      editModalLoading.value = true;
      courseInModal.value.categoryId1 = categoryIds.value[0];  // 保存之前先把两个分类从表单中提取出来
      courseInModal.value.categoryId2 = categoryIds.value[1];

      axios.post("/courseList/save", courseInModal.value).then((response) => {
        // console.log(response);
        const data = response.data;
        editModalLoading.value = false;


        if (data.success) {
          editModalVisible.value = false;

          // 重新加载列表
          handleQuery({
            current: courseListPagination.value.current,
            pageSize: courseListPagination.value.pageSize,
          });
        } else {
          message.error(response.data.message);
        }
      })
    };

    /**
     * 课程表格的编辑按钮
     */
    const btnEdit = (record: any) => {
      editModalVisible.value = true;
      courseInModal.value = Tool.copy(record);
      categoryIds.value = [courseInModal.value.categoryId1, courseInModal.value.categoryId2];  // 编辑时表单的分类显示需要再从 courseInModal 中提取出来
    };

    /**
     * 课程表格的删除按钮
     */
    const btnDeleteCourse = (id: number) => {
      axios.delete("/courseList/delete/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          // 重新加载列表
          handleQuery({
            current: courseListPagination.value.current,
            pageSize: courseListPagination.value.pageSize,
          });
        }
      })
    };

    /**
     * 新增课程按钮
     * 注: 这里不需要写具体的新增逻辑, 已经在对话框的"确认"按钮的逻辑中写过了
     */
    const btnAddCourse = () => {
      editModalVisible.value = true;
      courseInModal.value = {};  // 清空当前的数据信息
    };

    //-------------视频表单--------------
    const courseItemInModal = ref();
    const editItemModalVisible = ref(false);
    const editItemModalLoading = ref(false);

    /**
     * 视频表格的编辑按钮
     */
    const btnEditItem = (record: any) => {
      editItemModalVisible.value = true;
      courseItemInModal.value = Tool.copy(record);
    };

    /**
     * 视频表单的确定按钮
     */
    const editItemModalOK = () => {
      editItemModalLoading.value = true;

      axios.post("/courseItem/save", courseItemInModal.value).then((response) => {
        const data = response.data;

        if (data.success) {
          editItemModalLoading.value = false;
          editItemModalVisible.value = false;

          // 重新加载列表
          courseItemQuery({
            current: courseItemPagination.value.current,
            pageSize: courseItemPagination.value.pageSize,
          });
        } else {
          message.error(response.data.message);
        }
      })
    };

    /**
     * 课程表格的删除按钮
     */
    const btnDeleteItem = (id: number) => {
      axios.delete("/courseItem/delete/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          // 重新加载列表
          courseItemQuery({
            current: courseItemPagination.value.current,
            pageSize: courseItemPagination.value.pageSize,
          });
        }
      })
    };

    /**
     * 新增视频按钮
     * 注: 这里不需要写具体的新增逻辑, 已经在对话框的"确认"按钮的逻辑中写过了
     */
    const btnAddItem = () => {
      editItemModalVisible.value = true;
      courseItemInModal.value = {};  // 清空当前的数据信息
    };

    //-------------[分类]模块--------------
    /**
     * 分类数据查询
     */
    let categorys: any;
    const handleQueryCategory = () => {
      axios.get("/category/selectAll").then((response) => {
        if (response.data.success) {  // 判断后端接口返回是否出错
          categorys = response.data.content;
          categoryTree.value = Tool.array2Tree(response.data.content, 0);

          handleQuery({   // 下载列表的显示需要用到分类的信息, 由于 axios 是异步的, 所以必须在分类查询完成后再进行下载列表的查询显示
            current: courseListPagination.value.current,
            pageSize: courseListPagination.value.pageSize,
          });
        } else {
          message.error(response.data.message);
        }
      })
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

    onMounted(() => {
      handleQueryCategory();
      courseItemQuery({
        current: courseItemPagination.value.current,
        pageSize: courseItemPagination.value.pageSize,
      });
    });

    return {
      courseListLoading,
      courseList,
      courseListPagination,
      courseListColumns,
      courseItemPagination,
      courseItemColumns,
      courseItems,

      btnEdit,
      btnAddCourse,
      btnDeleteCourse,
      btnEditItem,
      courseTableChange,
      itemTableChange,
      getCategoryNameById,
      btnDeleteItem,
      btnAddItem,

      courseInModal,
      courseItemInModal,
      editModalVisible,
      editModalLoading,
      editItemModalVisible,
      editItemModalLoading,
      editModalOK,
      editItemModalOK,

      onSearch,

      categoryIds,
      categoryTree,
    };

  },
});
</script>

<style scoped>
.layout-content {
  padding: 30px 150px;
  width: 1200px;
  height: 1020px;
  min-height: 200px;
  margin: 20px auto 100px;
  overflow: hidden;
  background: rgb(244, 244, 244);
}

.course-list-table {
  height: 455px;
  padding-top: 10px;
}

.course-item-table {
  height: 455px;
  padding-top: 10px;
}

.course-list-info {
  font-size: 30px;
  padding-bottom: 10px;
}

</style>