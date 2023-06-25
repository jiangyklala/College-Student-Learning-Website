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

    <!--    下载管理表格-->
    <a-table
        :columns="columns"
        :data-source="listData"
        :row-key="record => record.id"
        :pagination="pagination" @change="handleTableChange"
        :loading="loading"
        bordered>
      <template v-slot:bodyCell="{ column, record, index }">
        <template v-if="column.dataIndex === 'category'">
          <span>{{ getCategoryNameById(record.categoryId1) }} / {{ getCategoryNameById(record.categoryId2) }}</span>
        </template>
        <template v-if="column.dataIndex === 'type'">
          <span>{{ getPracticeTypeName(record.type) }}</span>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <a-space size="small">
            <a-button type="link" @click="buttonEdit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="确认删除吗"
                ok-text="确认"
                cancel-text="取消"
                @confirm="buttonDelete(record.id)"
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
      title="下载表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form
        :model="practice"
        :label-col="{ span : 4 }"
    >
      <a-form-item label="题目描述">
        <a-input v-model:value="practice.name"/>
      </a-form-item>
      <a-form-item label="题目类型">
        <a-select
            ref="select"
            :v-model:value="practice.type"
            :placeholder="getPracticeTypeName(practice.type)"
            style="width: 120px"
            @change="practiceTypeSelectChange"
        >
          <!--          注意将 value 的类型改为 number 的格式-->
          <a-select-option :value=1>选择题</a-select-option>
          <a-select-option :value=2>判断题</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="分类">
        <!--        级联选择-->
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="categoryTree"/>
      </a-form-item>
      <a-form-item v-if="practice.type === 1" label="A选项">
        <a-input v-model:value="practice.content.optA"/>
      </a-form-item>
      <a-form-item v-if="practice.type === 1" label="B选项">
        <a-input v-model:value="practice.content.optB"/>
      </a-form-item>
      <a-form-item v-if="practice.type === 1" label="C选项">
        <a-input v-model:value="practice.content.optC"/>
      </a-form-item>
      <a-form-item v-if="practice.type === 1" label="D选项">
        <a-input v-model:value="practice.content.optD"/>
      </a-form-item>
      <a-form-item label="答案">
        <a-select
            ref="select"
            :v-model:value="practice.answer"
            :placeholder="getPracticeAnswerName(practice.answer)"
            style="width: 120px"
            @change="practiceAnswerNameSelectChange"
        >
          <!--          注意将 value 的类型改为 number 的格式-->
          <a-select-option v-if="practice.type === 1" :value=1>A</a-select-option>
          <a-select-option v-if="practice.type === 1" :value=2>B</a-select-option>
          <a-select-option v-if="practice.type === 1" :value=3>C</a-select-option>
          <a-select-option v-if="practice.type === 1" :value=4>D</a-select-option>


          <a-select-option v-if="practice.type === 2" :value=1>正确</a-select-option>
          <a-select-option v-if="practice.type === 2" :value=2>错误</a-select-option>
        </a-select>
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
  name: "AdminQuestion",
  setup: function () {


    const columns = [
      {
        title: '题目描述',
        dataIndex: 'name',
        width: '30%',
      },
      {
        title: '分类',
        // slots: {customRender: 'category'},
        dataIndex: 'category',
        width: '30%',
      },
      {
        title: '题目类型',
        dataIndex: 'type',
        width: '15%',
      },
      {
        title: '操作',
        dataIndex: 'action',
        width: '25%',
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
      axios.get("/practice/selectAll", {
        params: {
          page: p.current,
          size: p.pageSize,
          name: p.name,
        }
      }).then((response) => {

        if (response.data.success) {  // 判断后端接口返回是否出错
          loading.value = false;
          listData.value = response.data.content.list;  // 显示内容
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
      axios.get("/category/selectAllOBSort").then((response) => {
        if (response.data.success) {  // 判断后端接口返回是否出错
          categorys = response.data.content;
          categoryTree.value = Tool.array2Tree(response.data.content, 0);

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
      modalVisible.value = true;
      practice.value = {};  // 清空当前的数据信息
      practice.value.content = {};   // 显式指定题目的选项为一个 {}
    };

    /**
     * 表格的编辑按钮
     */
    const buttonEdit = (record: any) => {
      modalVisible.value = true;
      practice.value = Tool.copy(record);

      // 编辑时表单的分类显示需要再从 practice 中提取出来
      categoryIds.value = [practice.value.categoryId1, practice.value.categoryId2];

      // 将题目(practice) 的选项内容转为 JSON
      practice.value.content = JSON.parse(practice.value.content);
    };

    /**
     * 表格的删除按钮
     */
    const buttonDelete = (id: number) => {
      axios.delete("/practice/delete/" + id).then((response) => {
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
    const practice = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const categoryIds = ref();
    const categoryTree = ref();
    const questionContent = ref();

    /**
     * 表单确认按钮
     */
    const handleModalOk = () => {
      modalLoading.value = true;
      practice.value.categoryId1 = categoryIds.value[0];  // 保存之前先把两个分类从表单中提取出来
      practice.value.categoryId2 = categoryIds.value[1];
      practice.value.content = JSON.stringify(practice.value.content);
      console.log(practice.value);

      axios.post("/practice/save", practice.value).then((response) => {
        // console.log(response);
        const data = response.data;
        modalLoading.value = false;


        if (data.success) {
          modalVisible.value = false;

          // 重新加载列表
          QuestionListALlQuery({
            current: pagination.value.current,
            pageSize: pagination.value.pageSize,
          });
        } else {
          message.error(response.data.message);
        }
      })

    };

    /**
     * 题目类型选择
     */
    const practiceTypeSelectChange = (value: any) => {
      practice.value.type = value;
    }

    const practiceAnswerNameSelectChange = (value: any) => {
      practice.value.answer = value;
    }

    //-------------分页--------------

    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });

    /**
     * 分页的跳转页面处理
     * @param pagination
     */
    const handleTableChange = (pagination: any) => {
      // console.log("pagination:" + pagination);
      QuestionListALlQuery({
        current: pagination.current,
        pageSize: pagination.pageSize,
      });
    };


    //-------------搜索框--------------
    const onSearch = (searchValue: string) => {
      QuestionListALlQuery({
        current: 1,
        pageSize: pagination.value.pageSize,
        name: searchValue,
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

    const getPracticeTypeName = (type: number): string => {
      let res = '';
      switch (type) {
        case 1:
          res = '选择题';
          break;
        case 2:
          res = '判断题';
          break;
      }
      return res;
    }

    const getPracticeAnswerName = (type: number): string => {
      let res = '';
      switch (type) {
        case 1:
          res = 'A';
          break;
        case 2:
          res = 'B';
          break;
        case 3:
          res = 'C';
          break;
        case 4:
          res = 'D';
          break;
      }
      return res;
    }

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

      buttonEdit,
      addQuestionItem,
      buttonDelete,
      practiceTypeSelectChange,
      getPracticeTypeName,
      getPracticeAnswerName,
      practiceAnswerNameSelectChange,

      practice,
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
  padding: 70px 250px 0;
}
</style>