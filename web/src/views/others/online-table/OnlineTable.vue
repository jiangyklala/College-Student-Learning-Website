<template>
	<a-table :columns="columns" :data-source="data" @change="onChange"/>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {TableColumnType, TableProps} from "ant-design-vue";

export default defineComponent({
	components: {},
	name: 'OnlineTable',
	setup() {
		
		type TableDataType = {
			key: string;
			name: string;
			age: number;
			address: string;
		};
		
		type TableDataType1 = {
			company: string;
			recruitment_target: string;
			start_date: string;
			distance_from_start_date: string;
			end_date: string;
			distance_from_end_date: string;
			city: string;
			city_nature: string;
			deliver_address: string;
			extrapolation: string;
			important_events: string;
		};
		
		const columns: TableColumnType<TableDataType>[] = [
			{
				title: 'Name',
				dataIndex: 'name',
				filters: [
					{
						text: 'Joe',
						value: 'Joe',
					},
					{
						text: 'Jim',
						value: 'Jim',
					},
					{
						text: 'Submenu',
						value: 'Submenu',
						children: [
							{
								text: 'Green',
								value: 'Green',
							},
							{
								text: 'Black',
								value: 'Black',
							},
						],
					},
				],
				// specify the condition of filtering result
				// here is that finding the name started with `value`
				onFilter: (value: string | number | boolean, record: TableDataType) => {
					// Your filtering logic here
					return record.name.toString().indexOf(value.toString()) === 0;
				},
				sorter: (a: TableDataType, b: TableDataType) => a.name.length - b.name.length,
				sortDirections: ['descend'],
			},
			{
				title: 'Age',
				dataIndex: 'age',
				defaultSortOrder: 'descend',
				sorter: (a: TableDataType, b: TableDataType) => a.age - b.age,
			},
			{
				title: 'Address',
				dataIndex: 'address',
				filters: [
					{
						text: 'London',
						value: 'London',
					},
					{
						text: 'New York',
						value: 'New York',
					},
				],
				filterMultiple: false,
				onFilter: (value: string | number | boolean, record: TableDataType) => {
					return record.address.indexOf(value.toString()) === 0
				},
				sorter: (a: TableDataType, b: TableDataType) => a.address.length - b.address.length,
				sortDirections: ['descend', 'ascend'],
			},
		];
		
		const data: TableDataType[] = [
			{
				key: '1',
				name: 'John Brown',
				age: 32,
				address: 'New York No. 1 Lake Park',
			},
			{
				key: '2',
				name: 'Jim Green',
				age: 42,
				address: 'London No. 1 Lake Park',
			},
			{
				key: '3',
				name: 'Joe Black',
				age: 32,
				address: 'Sidney No. 1 Lake Park',
			},
			{
				key: '4',
				name: 'Jim Red',
				age: 32,
				address: 'London No. 2 Lake Park',
			},
		];
		
		const onChange: TableProps<TableDataType>['onChange'] = (pagination, filters, sorter) => {
			console.log('params', pagination, filters, sorter);
		};
		
		return {
			data,
			columns,
			
			onChange,
		}
	}
})
</script>

<style scoped>

</style>