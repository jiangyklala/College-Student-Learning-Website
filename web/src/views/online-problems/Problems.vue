<template>
	<a-layout-content class="layout-content">
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
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";
import {Tool} from "@/utils/tool";
import router from "@/router";

export default defineComponent({
	name: "Problems",
	setup() {
		
		//-------------页面--------------
		const mainLoading = ref();
		const listData = ref();
		
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
			
			window.open(routeData.href, '在线刷题');
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
		
		onMounted(() => {
			handleQueryCategory();
		})
		
		return {
			question,
			selectedOption,
			showResult,
			listData,
			mainLoading,
			categoryTree,
			
			getCategoryNameById,
			submitAnswer,
			examOnClick,
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
</style>