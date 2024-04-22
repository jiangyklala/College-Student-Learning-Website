<template>
	<a-layout-content class="layout-content">
		<div class="body">
			<a-space direction="horizontal" size="middle">
				<a-input-search
						placeholder="输入待搜索人员 name"
						enter-button="Search"
						size="middle"
						:loading="searchLoading"
						@search="onSearch"
				/>
			</a-space>
		</div>
		<br/>
	
	</a-layout-content>
	
	<!--  操作弹窗-->
	<a-modal
			title="人员权限表单"
			v-model:visible="modalVisible"
			:confirm-loading="modalLoading"
			width="25%"
			@ok="handleModalOk"
	>
		<a-form
				:model="wxInviter"
				:label-col="{ span: 16 }"
				:wrapper-col="{ span: 16 }"
				layout="vertical"
		>
			<a-form-item label="是否开通分销权限">
				<a-select v-model:value="wxInviter.isAccessible">
					<a-select-option :value="true">true</a-select-option>
					<a-select-option :value="false">false</a-select-option>
				</a-select>
			</a-form-item>
			<a-form-item label="分销比例(输入 1-100 间的整数)">
				<a-input v-model:value="wxInviter.earnRate"/>
			</a-form-item>
			<a-form-item label="邀请余额(未提现)">
				<a-input v-model:value="wxInviter.inviteBalance"/>
				<a-button type="dashed" @click="balanceTransferTap">全部划转</a-button>
			</a-form-item>
			<a-form-item label="已提现佣金">
				<a-input v-model:value="wxInviter.earnings"/>
			</a-form-item>
		</a-form>
	</a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import {message} from 'ant-design-vue';
import axios from 'axios';

export default defineComponent({
	components: {},
	name: "AdminUsers",
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
		const searchLoading = ref(false);
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
					
				} else {
					message.error(response.data.message);
				}
			})
		}
		
		
		//-------------表单--------------
		const wxInviter = ref();
		const modalLoading = ref(false);
		const modalVisible = ref(false);
		
		/**
		 * 表单确认按钮
		 */
		const handleModalOk = () => {
			modalLoading.value = true;
			
			
			axios.post("/wxUser/searchLimitsSubmit", wxInviter.value).then((response) => {
				const data = response.data;
				modalLoading.value = false;
				
				if (data.success) {
					modalVisible.value = false;
					
				} else {
					message.error(response.data.message);
				}
			})
			
		};
		
		const searchLimitsQuery = (searchValue: string) => {
			axios.post("/wxUser/searchLimits", {
				userName: searchValue
			}).then((response) => {
				const data = response.data;
				wxInviter.value = data.content;
				searchLoading.value = false;
				
				if (data.success) {
					modalVisible.value = true;
				} else {
					message.error(response.data.message);
				}
			});
		}
		
		const balanceTransferTap = () => {
			wxInviter.value.earnings += wxInviter.value.inviteBalance;
			wxInviter.value.inviteBalance = 0.0;
		}
		
		//-------------搜索框--------------
		const onSearch = (searchValue: string) => {
			searchLoading.value = true;
			searchLimitsQuery(searchValue);
		};
		
		
		// eslint-disable-next-line @typescript-eslint/no-empty-function
		onMounted(() => {
		
		});
		
		return {
			loading,
			listData,
			columns,
			searchLoading,
			
			wxInviter,
			modalVisible,
			modalLoading,
			handleModalOk,
			balanceTransferTap,
			
			onSearch,
		};
		
	},
});
</script>

<style scoped>
.layout-content {
	padding: 70px 80px 0;
}

.body {
	padding-top: 5%;
	padding-bottom: 5%;
	text-align: center;
}
</style>