<template>
  <a-layout-content class="layout-content">
    <div class="body">
      <a-space direction="horizontal" size="large">
        <a-input-search
            placeholder="输入待搜索名称"
            enter-button="Search"
            size="large"
            @search="onSearch"
        />
        <a-button type="primary" @click="addQuestionItem">
          新增
        </a-button>
      </a-space>
    </div>
    <br/>

    <!--    下载管理表格-->
    <a-table
        :columns="columns"
        :data-source="listData"
        :row-key="record => record.id"
        :pagination="pagination"
        @change="handleTableChange"
        :loading="loading"
        bordered>
      <template v-slot:bodyCell="{ column, record, index }">
        <template v-if="column.dataIndex === 'category'">
					<span>{{
              getParentCategoryNameById(record.categoryId) == "" ? "" : getParentCategoryNameById(record.categoryId) + " / "
            }}{{ getCategoryNameById(record.categoryId) }}</span>
        </template>

        <template v-if="column.dataIndex === 'action'">
          <a-space size="small">
            <a-button type="link" @click="buttonEdit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="确认删除吗, 删除之后用户对这道题花的积分就无意义了"
                ok-text="确认"
                cancel-text="取消"
                @confirm="buttonDelete(record)"
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
      title="编辑表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      width="65%"
      @ok="handleModalOk"
  >
    <a-form
        :model="question"
        :label-col="{ span : 4 }"
    >
      <a-form-item label="题目描述">
        <a-textarea
            v-model:value="question.title"
            :auto-size="{ minRows: 2, maxRows: 5 }"
        />
      </a-form-item>
      <a-form-item label="分类">
        <!--        级联选择-->
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="categoryTree"/>
      </a-form-item>
      <a-form-item label="文章答案(.md)">
        <a-textarea
            v-model:value="question.answer"
            :auto-size="{ minRows: 5, maxRows: 10 }"
        />
      </a-form-item>
      <a-form-item label="所需积分">
        <a-input-number v-model:value="question.points" :min="1" :max="50"/>
      </a-form-item>
      <a-form-item label="重要程度">
        <a-input-number v-model:value="question.importanceLevel" :min="1" :max="5"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, watch} from "vue";
import {message} from 'ant-design-vue';
import axios from 'axios';
import {Tool} from "@/utils/tool";

export default defineComponent({
  components: {},
  name: "AdminWxQuestion",
  setup: function () {

    let categoryFilterArray = ref();
    categoryFilterArray.value = [];

    const columns = [
      {
        title: '题目描述',
        dataIndex: 'title',
        width: '30%',
      },
      {
        title: '分类',
        // slots: {customRender: 'category'},
        dataIndex: 'category',
        width: '20%',
        filters: categoryFilterArray.value,
        filterSearch: true,
        onFilter: (value: number, record: any) => {
          return record.categoryId === value;
        },
      },
      {
        title: '点赞数',
        dataIndex: 'like',
        width: '9%',
      },
      {
        title: '收藏数',
        dataIndex: 'collect',
        width: '9%',
      },
      {
        title: '所需积分',
        dataIndex: 'points',
        width: '12%',
      },
      {
        title: '重要程度',
        dataIndex: 'importanceLevel',
        width: '12%',
      },
      {
        title: '操作',
        dataIndex: 'action',
        width: '15%',
      },
    ];


    //-------------页面--------------

    const loading = ref(true);
    const listData = ref();

    /**
     * 下载列表数据查询
     * @param p
     */
    const QuestionListALlQuery = (p: any) => {
      axios.post("/wxQuestion/selectAllAdmin", {
        page: p.current,
        size: p.pageSize,
        title: p.title,
        categoryIdList: p.categoryIdList,
      }).then((response) => {

        if (response.data.success) {  // 判断后端接口返回是否出错
          loading.value = false;
          listData.value = response.data.content.list;  // 显示内容(反转)
          // console.log(listData.value[0]);
          // listData.value[0].content = JSON.parse(listData.value[0].content);
          // console.log(listData.value[0]);

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
    const handleQueryCategory = () => {
      axios.get("/category/selectByTypeAndLevel/-1/-1").then((response) => {
        if (response.data.success) {  // 判断后端接口返回是否出错
          categorys = response.data.content;
          categoryTree.value = Tool.array2Tree(response.data.content, 0);
          nonChildrenDisabled();
          convert2CategoryFilterArray(response.data.content);
          // console.log(categoryTree.value);


          QuestionListALlQuery({   // 下载列表的显示需要用到分类的信息, 由于 axios 是异步的, 所以必须在分类查询完成后再进行下载列表的查询显示
            current: pagination.value.current,
            pageSize: pagination.value.pageSize,
          });
        } else {
          message.error(response.data.message);
        }
      })
    }


    //-------------表格--------------

    /**
     * 新增按钮
     * 注: 这里不需要写具体的新增逻辑, 已经在对话框的"确认"按钮的逻辑中写过了
     */
    const addQuestionItem = () => {
      question.value = {};  // 清空当前的数据信息
      question.value.answer = "";   // 显式指定题目的选项为一个 {}
      question.value.points = 10;   // 默认所需积分 10
      question.value.importanceLevel = 1;  // 默认重要程度 1
      categoryIds.value = [];
      // console.log(question);
      modalVisible.value = true;
    };

    /**
     * 表格的编辑按钮
     */
    const buttonEdit = (record: any) => {
      modalVisible.value = true;
      question.value = Tool.copy(record);

      // 编辑时表单的分类显示需要再从 question 中提取出来
      let categoryId = question.value.categoryId;
      categoryIds.value = [getParentCategoryIdById(categoryId), categoryId];

      // 查找题目对应的答案
      axios.get("/wxQuestion/selectAnswerAdmin", {
        params: {
          answerId: record.answerId,
        }
      }).then((response) => {
        if (response.data.success) {
          question.value.answer = response.data.content;
        } else {
          message.error(response.data.message);
        }
      });
    };

    /**
     * 表格的删除按钮
     */
    const buttonDelete = (record: any) => {
      axios.delete("/wxQuestion/delete", {
        data: {
          wxQuestionId: record.id,
          wxQuestionAnswerId: record.answerId,
        },
      }).then((response) => {
        const data = response.data;
        if (data.success) {
          // 重新加载列表
          QuestionListALlQuery({
            current: pagination.value.current,
            pageSize: pagination.value.pageSize,
          });
        }
      })
    };


    //-------------表单--------------
    const question = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const categoryIds = ref();
    const categoryTree = ref();

    /**
     * 表单确认按钮
     */
    const handleModalOk = () => {
      modalLoading.value = true;
      question.value.categoryId = categoryIds.value[categoryIds.value.length - 1];  // 保存之前先把两个分类从表单中提取出来
      // question.value.content = JSON.stringify(question.value.content);
      // console.log(categoryIds.value[categoryIds.value.length - 1]);

      axios.post("/wxQuestion/save", question.value).then((response) => {
        // console.log(response);
        const data = response.data;
        modalLoading.value = false;


        if (data.success) {
          modalVisible.value = false;

          // // 重新加载列表
          // QuestionListALlQuery({
          //   current: pagination.value.current,
          //   pageSize: pagination.value.pageSize,
          // });
        } else {
          message.error(response.data.message);
        }
      })

    };

    /**
     * 题目类型选择
     */
    const practiceTypeSelectChange = (value: any) => {
      question.value.type = value;
    }

    const practiceAnswerNameSelectChange = (value: any) => {
      question.value.answer = value;
    }

    const practiceLevelSelectChange = (value: any) => {
      question.value.level = value;
    }

    //-------------分页--------------

    const pagination = ref({
      current: 1,
      pageSize: 20,
      total: 0
    });

    /**
     * 分页的跳转页面处理
     * @param pagination
     */
    const handleTableChange = (pagination: any, filters: any, sorter: any, others: any) => {
      // console.log('Pagination:', pagination);
      // console.log('Filters:', filters);
      // console.log('others:', others);
      // 通过 others 中的 action 属性区分操作的对象

      QuestionListALlQuery({
        current: pagination.current,
        pageSize: pagination.pageSize,
        categoryIdList: filters.category,
      })
    };


    //-------------搜索框--------------
    const onSearch = (searchValue: string) => {
      QuestionListALlQuery({
        current: 1,
        pageSize: pagination.value.pageSize,
        title: searchValue,
      })
    };

    //-------------其它--------------

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
     * 根据目录id返回具体的父分类名称
     */
    const getParentCategoryNameById = (cid: number) => {
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          // 这里直接 return item.name 不起作用
          result = getCategoryNameById(item.parent);
        }
      });
      return result;
    }

    /**
     * 根据目录id返回具体的父分类id
     */
    const getParentCategoryIdById = (cid: number) => {
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          result = item.parent;
        }
      });
      return result;
    }

    // 将没有子分类的一级分类设置上 disabled : true
    const nonChildrenDisabled = () => {
      categoryTree.value.forEach((item: any) => {
        if (Tool.isEmpty(item.children)) {
          item.disabled = true;
        }
      })
    }

    const convert2CategoryFilterArray = (array: any[]) => {
      array.forEach((item: any) => {

        if (item.parent === 0) {
          return;
        }

        let itemJsonObject = {
          text: "",
          value: -1,
        };

        itemJsonObject.value = item.id;
        itemJsonObject.text = getParentCategoryNameById(item.id) + "/" + item.name;

        categoryFilterArray.value.push(itemJsonObject);
      })
    }

    //-------------其它--------------
    // 监听新增题目时, 题目所属模块的变化
    watch(categoryIds, () => {
      categorys.forEach((item: any) => {
        if (item.id === categoryIds.value[0]) {
          // 如果所属模块为 大厂面经, 默认需要积分改为 30
          if (item.type === 2) question.value.points = 30;
        }
      });
    });


    onMounted(() => {
      handleQueryCategory();
    });

    return {
      loading,
      listData,
      pagination,
      columns,
      handleTableChange,
      getCategoryNameById,
      getParentCategoryNameById,

      buttonEdit,
      addQuestionItem,
      buttonDelete,
      practiceTypeSelectChange,
      practiceAnswerNameSelectChange,
      practiceLevelSelectChange,

      question,
      modalVisible,
      modalLoading,
      handleModalOk,

      onSearch,

      categoryIds,
      categoryTree,
    };

  },
});
</script>

<style scoped>
.layout-content {
  padding: 70px 80px 0;
}
</style>