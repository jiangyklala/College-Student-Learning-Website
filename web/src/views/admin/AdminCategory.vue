<template>
  <a-layout-content class="layout-content">
    <div class="body">
      <a-space direction="horizontal" size="large">
	      <a-button type="primary" @click="addCategoryItem">
		      新增
	      </a-button>
	      <a-button type="primary" @click="resetCount">
		      重置分类对应的题目总数
	      </a-button>
      </a-space>
    </div>
    <a-table
        :columns="columns"
        :data-source="tableData"
        :row-key="record => record.id"
        :loading="loading"
        :pagination="false"
        bordered>
      <template v-slot:bodyCell="{ column, record, index }">
	      <!--	      分类图标显示-->
	      <template v-if="column.dataIndex === 'avatar'">
		      <a-image
				      :width="40"
				      :src="record.avatarLink"
		      />
	      </template>
	      
	      <!--	      模块分类显示列-->
	      <template v-if="column.dataIndex === 'type'">
		      <span>{{ getCategoryModalName(record.type) }}</span>
	      </template>
	      
	      <!--	      操作列-->
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
      title="分类表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form
		    :model="category"
		    :label-col="{ span : 6 }"
    >
      <a-form-item label="名称">
        <a-input v-model:value="category.name"/>
      </a-form-item>
      <a-form-item label="父分类">
        <a-select
            ref="select"
            v-model:value="category.parent"
        >
          <a-select-option value="0">
	          无
          </a-select-option>
	        <a-select-option v-for="i in tableData"
	                         :key="i.id"
	                         :value="i.id"
	                         :disabled="category.id === i.id">
		        {{ i.name }}
	        </a-select-option>
        </a-select>
      </a-form-item>
	    <a-form-item label="图片链接">
		    <a-textarea
				    v-model:value="category.avatarLink"
				    :auto-size="{ minRows: 2, maxRows: 5 }"
		    />
	    </a-form-item>
	    <a-form-item label="题目所属模块">
		    <a-select
				    v-model:value="category.type"
				    style="width: 150px"
		    >
			    <a-select-option :value="1">普通面试题</a-select-option>
			    <a-select-option :value="2">大厂面经</a-select-option>
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
  name: "AdminCategory",
  setup: function () {

    const columns = [
	    {
		    title: '名称',
		    dataIndex: 'name',
		    width: '20%',
	    },
	    {
		    title: '分类图片',
		    dataIndex: 'avatar',
		    width: '15%',
	    },
	    {
		    title: '分类所属模块',
		    dataIndex: 'type',
		    width: '25%',
	    },
	    {
		    title: '总数',
		    dataIndex: 'total',
		    width: '10%',
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

    /**
     * 总分类数据查询
     */
    const categoryAllOBSortQuery = () => {
      loading.value = true;
	    axios.get("/category/selectByTypeAndLevel/-1/-1").then((response) => {
		    
		    if (response.data.success) {
			    loading.value = false;
			    // tableData.value = [];
			    tableData.value = Tool.array2Tree(response.data.content, 0)
			    console.log(tableData.value);
			    
		    } else {
			    message.error(response.data.message);
		    }
      });
    }

    //-------------表格--------------
	  
	  /**
	   * 新增按钮
	   * 注: 这里不需要写具体的新增逻辑, 已经在对话框的"确认"按钮的逻辑中写过了
	   */
	  const addCategoryItem = () => {
		  category.value = {};  // 清空当前的数据信息
		  // category.value.type = 1;   // 设置为, 添加到微信小程序的分类
		  modalVisible.value = true;
	  };
	  
	  const resetCount = () => {
		  axios.get("/category/resetCount").then((response) => {
			  
			  if (response.data.success) {
				  // 重新加载列表
				  categoryAllOBSortQuery();
				  message.success("reset success!");
			  } else {
				  message.error(response.data.message);
			  }
		  });
	  }
	  
	  /**
	   * 表格的编辑按钮
	   */
	  const buttonEdit = (record: any) => {
		  modalVisible.value = true;
		  category.value = Tool.copy(record);
	  };
	  
	  /**
     * 表格的删除按钮
     */
    const buttonDelete = (id: number) => {
      axios.delete("/category/delete/" + id).then((response) => {
        const data = response.data;

        if (data.success) {
          // 重新加载列表
          categoryAllOBSortQuery();
        }
      })
    };

    //-------------表单--------------
	  const category = ref();
	  const modalVisible = ref(false);
    const modalLoading = ref(false);

    /**
     * 表单确认按钮
     */
    const handleModalOk = () => {
	    modalLoading.value = true;
	    
	    console.log(category.value.parent);
	    console.log(category.value.parent != "0");
	    console.log(category.value.parent != '0');
	    console.log(category.value.parent != 0);
	    if (category.value.parent != 0) {
		    category.value.level = 1;
	    }
	    console.log(category.value);
	    axios.post("/category/save", category.value).then((response) => {
		    // console.log(response);
		    const data = response.data;
		    modalLoading.value = false;
		    
		    
		    if (data.success) {
			    modalVisible.value = false;
			    
			    // 重新加载列表
			    categoryAllOBSortQuery();
		    } else {
			    message.error(response.data.message);
		    }
	    })
	    
    };
	  
	  const getCategoryModalName = (type: number) => {
		  switch (type) {
			  case 1:
				  return "普通面试题";
			  case 2:
				  return "大厂面经";
		  }
	  }
	  
	  
	  onMounted(() => {
		  categoryAllOBSortQuery();
	  });
	  
	  return {
		  loading,
		  tableData,
		  listData,
		  columns,
		  
		  buttonEdit,
		  addCategoryItem,
		  resetCount,
		  buttonDelete,
		  
		  category,
		  modalVisible,
		  modalLoading,
		  handleModalOk,
		  getCategoryModalName,
		  
	  };

  },
});
</script>

<style scoped>
.layout-content {
	padding: 70px 50px 250px
}
</style>