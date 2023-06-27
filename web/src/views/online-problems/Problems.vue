<template>
	<a-layout-content class="layout-content">
		<div class="practiceSettings-div" @click="practiceSettingsClick">
			<setting-outlined/>
			题目设置
		</div>
		<span v-for="categoryNode in categoryTree" v-bind:key="categoryNode">
       <div class="mainTags-div">
        <tag-two-tone/>
        {{ categoryNode.name }}
       </div>
      <a-row :gutter="16">
        <div v-for="category in categoryNode.children" v-bind:key="category">
          <a-col class="gutter-row" :span="6">
            <a-card style="width: 180px; background-color: rgb(246, 246, 246)" @click="examOnClick(category)">
              <a-card-meta :title="category.name">

              </a-card-meta>
            </a-card>
          </a-col>
        </div>
      </a-row>

    </span>
	</a-layout-content>
	
	<a-modal v-model:visible="practiceSettingsVisible"
	         width="700px"
	         title="刷题设置"
	         @ok="practiceSettingsModalOK">
		<a-form
				:model="practiceSettingsState"
				name="basic"
				:label-col="{ span: 4 }"
		>
			<a-form-item label="题目数量" :wrapper-col="{ offset: 1, span: 16 }">
				<a-radio-group v-model:value="practiceSettingsState.problemCount" name="problemCountGroup">
					<a-radio value="FIVE">5</a-radio>
					<a-radio value="TEN">15</a-radio>
					<a-radio value="FIFTEEN">20</a-radio>
					<a-radio value="TWENTY">25</a-radio>
					<a-radio value="THIRTY">30</a-radio>
				</a-radio-group>
			</a-form-item>
			
			<a-form-item label="题目来源" :wrapper-col="{ offset: 1, span: 16 }">
				<a-radio-group v-model:value="practiceSettingsState.problemSource" name="problemSourceGroup">
					<a-radio value="NEW">只出新题</a-radio>
					<a-radio value="WRONGANDNEW">错题 + 新题</a-radio>
					<a-radio value="WRONG">只出错题</a-radio>
					<a-radio value="ALL">不限来源</a-radio>
				</a-radio-group>
			</a-form-item>
			
			<a-form-item label="题目难度" :wrapper-col="{ offset: 1, span: 16 }">
				<a-radio-group v-model:value="practiceSettingsState.problemLevel" name="problemLevelGroup">
					<a-radio value="BEGINNER">入门</a-radio>
					<a-radio value="EASY">简单</a-radio>
					<a-radio value="INTERMEDIATE">中等</a-radio>
					<a-radio value="DIFFICULT">较难</a-radio>
					<a-radio value="CHALLENGING">困难</a-radio>
				</a-radio-group>
			</a-form-item>
		
		</a-form>
	</a-modal>

</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/utils/tool";
import router from "@/router";
import store from "@/store";

interface PracticeSettingsState {
	problemCount: string;
	problemSource: string;
	problemLevel: string;
}

export default defineComponent({
	name: "Problems",
	setup() {
		
		//-------------页面--------------
		const mainLoading = ref();
		const listData = ref();
		const userID = computed(() => {
			return store.state.userInfo.id;
		});
		
		/**
		 * 题目列表查询
		 */
		const practiceListAllGpByCgId1Query = () => {
			axios.get("/practice/selectAllGpByCgId1", {}).then((response) => {
				
				if (response.data.success) {
					mainLoading.value = false;
					listData.value = response.data.content;
					
					console.log(listData);
					
					// autoLayoutHeight();   // 自动调整布局
					
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
			axios.get("/category/selectPracticeOBSort").then((response) => {
				// loading.value = false;
				if (response.data.success) {  // 判断后端接口返回是否出错
					categorys = response.data.content;
					categoryTree.value = Tool.array2Tree(response.data.content, 0);
					console.log(categoryTree);
					
					// practiceListAllGpByCgId1Query();
					
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
		
		//-------------分类点击--------------
		
		/**
		 * 点击分类跳转的刷题界面
		 * @param categoryItem
		 */
		const examOnClick = (categoryItem: any) => {
			console.log(categoryItem);
			const routeData = router.resolve({
				path: "/online-problems/DoExam",
			});
			
			sessionStorage.setItem("ExamCategory", JSON.stringify(categoryItem));                   // 临时存储
			
			// window.open(routeData.href, '在线刷题');
			// 页内跳转
			router.push('/online-problems/DoExam');
		}
		
		const practiceSettingsVisible = ref(false);
		const practiceSettingsState = ref();
		practiceSettingsState.value = [];
		
		const practiceSettingsClick = () => {
			practiceSettingsState.value.problemCount = settingsInfo.value.settingsObj.problemCount;
			practiceSettingsState.value.problemSource = settingsInfo.value.settingsObj.problemSource;
			practiceSettingsState.value.problemLevel = settingsInfo.value.settingsObj.problemLevel;
			practiceSettingsVisible.value = true;
		}
		
		// const practiceSettingsState = reactive<PracticeSettingsState>({
		// 	problemCount: settingsInfo.value.problemCount,
		// 	problemSource: 1,
		// 	problemLevel: 1,
		// });
		
		const practiceSettingsModalOK = () => {
			console.log("Ok");
		}
		
		const question = {
			title: '这是一个问题的标题',
			options: [
				{id: 1, label: '选项1'},
				{id: 2, label: '选项2'},
				{id: 3, label: '选项3'},
				{id: 4, label: '选项4'}
			],
			correctOption: 2
		};
		
		const selectedOption = ref<number>(0);
		
		const showResult = ref(false);
		
		const submitAnswer = () => {
			showResult.value = true;
		}
		
		const settingsInfo = ref();
		const settingsInfoQuery = () => {
			axios.get("/practice/selectSettingsInfo/" + userID.value).then((response) => {
				let data = response.data;
				if (data.success) {
					console.log(data);
					settingsInfo.value = data.content;
					// settingsInfo.value.content = JSON.parse(settingsInfo.value.content);
				} else {
					message.error(data.message);
				}
			})
		}
		
		onMounted(() => {
			handleQueryCategory();
			settingsInfoQuery();
		})
		
		return {
			question,
			selectedOption,
			showResult,
			listData,
			mainLoading,
			categoryTree,
			
			practiceSettingsState,
			practiceSettingsVisible,
			
			getCategoryNameById,
			submitAnswer,
			examOnClick,
			practiceSettingsClick,
			practiceSettingsModalOK,
		}
	}
})
</script>

<style scoped>
.layout-content {
	padding: 70px 150px;
	width: 1200px;
	height: 100%;
	min-height: 1000px;
	margin: 20px auto 100px;
	overflow: hidden;
	background: #fff;
}

.mainTags-div {
	padding-top: 20px;
	width: 300px;
	height: 80px;
	font-weight: 700;
	font-size: 25px;
}

.practiceSettings-div {
	font-size: 20px;
	color: #7d8293;
}
</style>