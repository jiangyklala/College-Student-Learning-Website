<template>
	拉来阿拉拉阿拉
</template>

<script lang="ts">
import {computed, defineComponent, onMounted} from "vue";
import store from "@/store";
import axios from "axios";
import {message} from "ant-design-vue";

export default defineComponent({
	components: {},
	name: "Admin",
	setup: function () {
		
		const userInfo = computed(() => {
			return store.state.userInfo;
		});
		
		const checkAdminPermission = () => {
			axios.post("/user/checkAdminPermission", {
				userId: userInfo.value.id,
			}).then((response) => {
				
				if (response.data.success) {
					message.success("权限验证成功")
					
				} else {
					message.error(response.data.message);
				}
			})
		}
		
		onMounted(() => {
			checkAdminPermission();
		});
		
		return {}
	}
})
</script>

<style scoped>

</style>