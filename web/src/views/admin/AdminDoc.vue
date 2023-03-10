<template>
  <a-layout-content class="layout-content">
    <div class="body">
      <a-space direction="horizontal" size="large">
        <a-button type="primary" @click="addCategoryItem">
          新增
        </a-button>
      </a-space>
    </div>
    <a-table
        :columns="columns"
        :data-source="tableData"
        :row-key="record => record.id"
        :loading="loading"
        :pagination="false"
        bordered
        class="doc-table"
    >
      <template v-slot:bodyCell="{ column, record, index }">
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
  <a-modal
      title="文档表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form
        :model="doc"
        :label-col="{ span : 4 }"
    >
      <a-form-item label="名称">
        <a-input v-model:value="doc.name"/>
      </a-form-item>
      <a-form-item label="父文档">
        <a-tree-select
            v-model:value="doc.parent"
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            placeholder="Please select"
            tree-default-expand-all
            :tree-data="treeSelectData"
            :fieldNames="{label: 'name', key:'id', value:'id'}"
        >
        </a-tree-select>
      </a-form-item>
      <a-form-item label="排序">
        <a-input v-model:value="doc.sort"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from "vue";
import {message} from 'ant-design-vue';
import axios from 'axios';
import {Tool} from "@/utils/tool";

export default defineComponent({
  components: {},
  name: "AdminDoc",
  setup: function () {

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        width: '40%',
      },
      {
        title: '父文档',
        dataIndex: 'parent',
        width: '20%',
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


    //-------------页面--------------

    const loading = ref(true);
    const listData = ref();
    const tableData = ref();
    const columnId = ref();

    /**
     * 文档数据查询
     */
    const docByColumnIdQuery = (columnId : any) => {
      loading.value = true;
      axios.get("/doc/selectByColumnId/" + columnId).then((response) => {

        if (response.data.success) {  // 判断后端接口返回是否出错
          loading.value = false;
          tableData.value = Tool.array2Tree(response.data.content, 0);

        } else {
          message.error(response.data.message);
        }
      });
    }

    const init = () => {
      columnId.value = sessionStorage.getItem("ColumnId");
    }

    //-------------表格--------------

    const treeSelectData = ref();
    treeSelectData.value = [];
    const deleteIdStr : Array<string> = [];

    /**
     * 新增按钮
     * 注: 这里不需要写具体的新增逻辑, 已经在对话框的"确认"按钮的逻辑中写过了
     */
    const addCategoryItem = () => {
      doc.value = {                                             // 清空当前的数据信息, 避免冗余显示上一次编辑的内容
        columnId: columnId.value,                               // 记得赋值所属专栏Id
      };
      treeSelectData.value = Tool.copy(tableData.value);        // 更新选择树
      deleteParent(treeSelectData.value);                       // 这里还需删除 parent 字段
      treeSelectData.value.unshift({id: 0, name: '无'});  // 为树最前面添加一个"0"级分类, "无"

      modalVisible.value = true;
    };

    /**
     * 表格的编辑按钮
     */
    const buttonEdit = (record: any) => {
      modalVisible.value = true;
      doc.value = Tool.copy(record);

      treeSelectData.value = Tool.copy(tableData.value);
      // 不能选择自己以及自己的子节点
      setDisable(treeSelectData.value, record.id);
      // 不能选择自己的直接父节点, 没意义
      setParentDocDisable(treeSelectData.value, record.parent);
      deleteParent(treeSelectData.value);                       // bug: 不能有 parent 字段, 否则会出现: [vue warn]: invalid prop: type check failed for prop "parent". expected object, got number.


      treeSelectData.value.unshift({id: 0, name: '无'});  // 为树最前面添加一个"0"级分类, "无"

      // console.log(treeSelectData.value);
    };

    /**
     * 表格的删除按钮
     */
    const buttonDelete = (id: number) => {
      getDeleteIdStr(tableData.value, id);
      console.log(deleteIdStr.join(","));
      axios.delete("/doc/deleteIdStr/" + deleteIdStr.join(",")).then((response) => {
        const data = response.data;

        if (data.success) {
          // 重新加载列表
          docByColumnIdQuery(columnId.value);
        }
      })
    };

    //-------------表单--------------
    const doc = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    /**
     * 表单确认按钮
     */
    const handleModalOk = () => {
      modalLoading.value = true;
          // columnId = columnId.value;
      console.log(doc);
      // doc.value.columnId = columnId.value;

      axios.post("/doc/save", doc.value).then((response) => {
        // console.log(response);
        const data = response.data;
        modalLoading.value = false;


        if (data.success) {
          modalVisible.value = false;

          // 重新加载列表
          docByColumnIdQuery(columnId.value);
        } else {
          message.error(response.data.message);
        }
      })

    };


    //-------------其它--------------

    /**
     * 将选择树中指定 id 节点及其子节点都添加 disabled = true 的属性
     * @param treeSelectData
     * @param id
     */
    const setDisable = (treeSelectData : any, id : any) => {
      for (let i = 0; i < treeSelectData.length; ++i) {
        const node = treeSelectData[i];
        if (node.id === id) {
          node.disabled = true;

          if (Tool.isNotEmpty(node.children)) {
            for (let j = 0; j < node.children.length; ++j) {
              setDisable(node.children, node.children[j].id);
            }
          }

        } else if (Tool.isNotEmpty(node.children)) {
          setDisable(node.children, id);
        }
      }
    }

    /**
     * 将 id 与给定 parentId 相同的节点添加 disabled = true 的属性
     * @param treeSelectData
     * @param parentId
     */
    const setParentDocDisable = (treeSelectData : any, parentId : any) => {
      for (let i = 0; i < treeSelectData.length; ++i) {
        const node = treeSelectData[i];
        if (node.id === parentId) {
          node.disabled = true;
          break;
        }
        if (Tool.isNotEmpty(node.children)) {
          setParentDocDisable(node.children, parentId);
        }
      }
    }

    /**
     * 将选择树中的所有节点的 parent 属性删除
     * @param treeSelectData
     */
    const deleteParent = (treeSelectData : any) => {
      for (let i = 0; i < treeSelectData.length; ++i) {
        delete treeSelectData[i].parent;
        if (Tool.isNotEmpty(treeSelectData[i].children)) {
          deleteParent(treeSelectData[i].children);
        }
      }
      // console.log(treeSelectData);
    }

    /**
     * 将选择树中指定 id 节点及其子节点都的 id 都添加到 deleteIdStr 中
     * @param treeSelectData
     * @param id
     */
    const getDeleteIdStr = (treeSelectData : any, id : any) => {
      for (let i = 0; i < treeSelectData.length; ++i) {
        const node = treeSelectData[i];
        if (node.id === id) {
          deleteIdStr.push(id);  // 将目标 id 放入结果集

          if (Tool.isNotEmpty(node.children)) {
            for (let j = 0; j < node.children.length; ++j) {
              getDeleteIdStr(node.children, node.children[j].id);
            }
          }

        } else if (Tool.isNotEmpty(node.children)) {
          getDeleteIdStr(node.children, id);
        }
      }
    }



    onMounted(() => {
      init();
      // console.log(columnId);
      docByColumnIdQuery(columnId.value);
    });

    return {
      loading,
      tableData,
      listData,
      columns,
      treeSelectData,

      buttonEdit,
      addCategoryItem,
      buttonDelete,

      doc,
      modalVisible,
      modalLoading,
      handleModalOk,

    };

  },
});
</script>

<style scoped>

.layout-content {
  padding: 30px;
  width: 1200px;
  height: 1800px;
  min-height: 200px;
  margin: 20px auto 100px;
  overflow: hidden;
  background: rgb(237,239,242);
}

.doc-table {
  width: 1100px;
}

</style>