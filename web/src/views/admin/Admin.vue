<template>
	<a-layout-content class="layout-content">
		<div v-if="showAll">
			<!--			<a-button>-->
			<!--				<router-link to="/admin/AdminDownload">下载管理</router-link>-->
			<!--			</a-button>-->
			<!--			<br><br>-->
			<!--			<a-button>-->
			<!--				<router-link to="/admin/AdminCourse">课程及视频管理</router-link>-->
			<!--			</a-button>-->
			<!--			<br><br>-->
			<a-button>
				<router-link to="/admin/AdminCategory">分类管理</router-link>
			</a-button>
			<!--			<br><br>-->
			<!--			<a-button>-->
			<!--				<router-link to="/admin/AdminColumn">专栏管理</router-link>-->
			<!--			</a-button>-->
			<!--			<br><br>-->
			<!--			<a-button>-->
			<!--				<router-link to="/admin/AdminQuestion">刷题管理</router-link>-->
			<!--			</a-button>-->
			<br><br>
			<a-button>
				<router-link to="/admin/AdminWxQuestion">微信题目管理</router-link>
			</a-button>
			<br><br>
		</div>
	</a-layout-content>
</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref} from "vue";
import store from "@/store";
import axios from "axios";
import {message} from "ant-design-vue";
import router from "@/router";

export default defineComponent({
	components: {},
	name: "Admin",
	setup: function () {
		
		const showAll = ref(false);
		
		const userInfo = computed(() => {
			return store.state.userInfo;
		});
		
		const checkAdminPermission = () => {
			axios.post("/user/checkAdminPermission", {
				userId: userInfo.value.id,
			}).then((response) => {
				
				if (response.data.success) {
					showAll.value = true;
					message.success("权限验证成功")
					
				} else {
					message.error(response.data.message);
				}
			})
		}
		
		const onTap = (path: any) => {
			const routeData = router.resolve({
				path: path,
			});
			
			window.open(routeData.href, '页面管理');
		}
		
		onMounted(() => {
			setTimeout(checkAdminPermission, 500)
		});
		
		return {
			showAll,
			
			onTap,
		}
	}
})
</script>

<style scoped>
.layout-content {
	padding: 70px 250px 0;
}
</style>