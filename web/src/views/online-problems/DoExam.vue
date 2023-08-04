<template>
	<a-layout-content class="layout-content">
		<span v-for="(problem, index) in problems" v-bind:key="index">
			<a-card style="width: 800px">
				<p>{{ problem.name }}</p>
					<a-radio-group v-model:value="answers[index]" name="radioGroup" @change="radioGroupChange">
						<template v-if="problem.type === 1">
              <a-radio value="1">{{ problem.content.optA }}</a-radio>
              <a-radio value="2">{{ problem.content.optB }}</a-radio>
              <a-radio value="3">{{ problem.content.optC }}</a-radio>
              <a-radio value="4">{{ problem.content.optD }}</a-radio>
						</template>
						<template v-if="problem.type === 2">
							<a-radio value="1">对</a-radio><br>
              <a-radio value="2">错</a-radio>
						</template>
        </a-radio-group>
      </a-card>
			<div style="height: 30px">
<!--			填充-->
			</div>
		</span>
		
		<div style="height: 30px">
			<!--			填充-->
		</div>
		<a-button type="primary" class="submit-btn">提交</a-button>
	
	</a-layout-content>
</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref} from "vue";
import axios from "axios";
import {message} from "ant-design-vue";
import store from "@/store";

export default defineComponent({
	name: "DoExam",
	setup() {
		//-------------页面--------------
		
		const extractProblemSettingJSON = ref();
		extractProblemSettingJSON.value = [];
		const userID = computed(() => {
			return store.state.userInfo.id;
		});
		const problems = ref(); // 题目数组
		const answers = ref();  // 答案数组
		answers.value = [];
		
		/**
		 * 页面初始化
		 */
		const initData = () => {
			let extractProblemSetting: any;
			extractProblemSetting = sessionStorage.getItem("ExtractProblemSetting");
			console.log(extractProblemSetting);
			if (extractProblemSetting != null) {
				extractProblemSettingJSON.value = JSON.parse(extractProblemSetting);
				console.log(extractProblemSettingJSON.value);
			}
			// getCourseItemByCourse(itemCourse);
		}
		
		const showProblems = () => {
			axios.get("/practice/extractProblems", {
				params: {
					"userId": userID.value,
					"problemCount": extractProblemSettingJSON.value.problemCount,
					"problemLevel": extractProblemSettingJSON.value.problemLevel,
					"categoryId2": extractProblemSettingJSON.value.categoryId2,
					"problemSource": extractProblemSettingJSON.value.problemSource,
				}
			}).then((response) => {
				
				if (response.data.success) {
					
					problems.value = response.data.content;
					for (const problem of problems.value) {
						problem.content = JSON.parse(problem.content);
					}
					console.log(response.data);
					
				} else {
					message.error(response.data.message);
				}
			})
		}
		
		const radioGroupChange = () => {
			// console.log(answers.value);
		}
		
		onMounted(() => {
			initData();
			showProblems();
		})
		
		return {
			problems,
			answers,
			
			radioGroupChange,
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

.submit-btn {
	width: 10%;
	margin-left: 45%;
}
</style>