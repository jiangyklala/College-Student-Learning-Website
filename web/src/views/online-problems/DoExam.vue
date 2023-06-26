<template>
	<p>afaf</p>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";

export default defineComponent({
	name: "DoExam",
	setup() {
		//-------------页面--------------
		
		let itemCourse: any;
		const examCategoryJSON = ref();
		examCategoryJSON.value = [];
		const courseItem = ref();
		const isLive = ref(false);
		let categoryData = ref([]);
		
		/**
		 * 页面初始化
		 */
		const initData = () => {
			let examCategoryInfo: any;
			examCategoryInfo = sessionStorage.getItem("ExamCategory");
			console.log(examCategoryInfo);
			if (examCategoryInfo != null) {
				examCategoryJSON.value = JSON.parse(examCategoryInfo);
				console.log(examCategoryJSON.value);
			}
			// getCourseItemByCourse(itemCourse);
		}
		
		onMounted(() => {
			initData();
		})
		
		return {}
	}
})
</script>

<style scoped>

</style>