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
		
		<!--    管理表格-->
		<a-table
				:columns="columns"
				:data-source="listData"
				:row-key="record => record.id"
				:pagination="pagination" @change="handleTableChange"
				:loading="loading"
				bordered>
			<template v-slot:bodyCell="{ column, record, index }">
				
				<template v-if="column.dataIndex === 'action'">
					<a-space size="small">
						<a-button type="link" @click="buttonEdit(record)">
							编辑
						</a-button>
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
			<a-form-item label="页面标题  ">
				<a-textarea
						v-model:value="question.title"
						:auto-size="{ minRows: 2, maxRows: 5 }"
				/>
			</a-form-item>
			<a-form-item label="页面内容(.md)">
				<a-textarea
						v-model:value="question.answer"
						:auto-size="{ minRows: 5, maxRows: 30 }"
				/>
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
	name: "AdminWxSpecialPage",
	setup: function () {
		
		
		const columns = [
			{
				title: '页面标题',
				dataIndex: 'title',
				width: '30%',
			},
			{
				title: '操作',
				dataIndex: 'action',
				width: '1%',
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
			axios.get("/wxSpecial/selectAllAdmin", {
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
		};
		
		/**
		 * 表格的删除按钮
		 */
		const buttonDelete = (record: any) => {
			axios.delete("/wxSpecial/delete", {
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
			
			
			axios.post("/wxSpecial/save", question.value).then((response) => {
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
				title: searchValue,
			})
		};
		
		
		onMounted(() => {
			
			QuestionListALlQuery({
				current: pagination.value.current,
				pageSize: pagination.value.pageSize,
			});
			
		});
		
		return {
			loading,
			listData,
			pagination,
			columns,
			handleTableChange,
			
			buttonEdit,
			addQuestionItem,
			buttonDelete,
			
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