<template>
	<a-layout-content class="layout-content">
		<a-space direction="vertical">
			会员购买价格(分)
			<a-input v-model:value="configList.yt_wa_cf_vp"/>
			<a-divider/>
			
			支付页面是否展示(0:不展示 1:展示)
			<a-input v-model:value="configList.yt_wa_cf_pps"/>
			<a-divider/>

      邀请码的优惠价格(分)
			<a-input v-model:value="configList.yt_wa_cf_id"/>
			<a-divider/>
			
			<a-button type="primary"
			          @click="configSubmitTap"
			          shape="round" :loading="configSubmitBtnLoading">保存变更
			</a-button>
		</a-space>
	
	</a-layout-content>

</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import axios from 'axios';
import {message} from 'ant-design-vue';

export default defineComponent({
	components: {},
	name: "AdminConfig",
	setup: function () {
		
		const configList = ref();
		configList.value = [];
		const configSubmitBtnLoading = ref(false);
		
		const allConfigName = ["id", "vp", "pps"];
		
		const selectAllConfig = () => {
			axios.post("/other/selectAllConfig", {
				prefix: "yt:wa:cf:",
				name: allConfigName,
			}).then((response) => {
				const data = response.data;
				
				if (data.success) {
					configList.value = JSON.parse(data.content);
					
				} else {
					message.error(response.data.message);
				}
			})
		}
		
		const configSubmitTap = () => {
			configSubmitBtnLoading.value = true;
			
			let mSetArray: string[] = new Array(0);
			// 组装 mset 命令参数
			Object.keys(configList.value).forEach(key => {
				mSetArray.push(key.replaceAll("_", ":"));  // key
				mSetArray.push(configList.value[key]);  // value
			})
			
			axios.post("/other/setConfigList", mSetArray)
					.then((response) => {
						const data = response.data;
						
						if (data.success) {
							configSubmitBtnLoading.value = false;
							message.success("保存成功");
						} else {
							message.error(response.data.message);
						}
					})
					.then(() => {
						selectAllConfig();  // 重新加载
					})
		}
		
		onMounted(() => {
			selectAllConfig();
		});
		
		return {
			configList,
			
			configSubmitBtnLoading,
			
			configSubmitTap,
		};
		
	},
});
</script>

<style scoped>
.layout-content {
	padding: 120px 80px 80px;
	text-align: center;
}
</style>


