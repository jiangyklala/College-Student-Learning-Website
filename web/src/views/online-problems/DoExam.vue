<template>
	<p>afaf</p>
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
					
					console.log(response.data);
					
				} else {
					message.error(response.data.message);
				}
			})
		}
		
		onMounted(() => {
			initData();
			showProblems();
		})
		
		return {}
	}
})
</script>

<style scoped>

</style>