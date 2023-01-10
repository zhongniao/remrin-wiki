<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-table
          :columns="columns"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange">

        <template #headerCell="{ title, column }"></template>
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'cover'">
            <a-image
              :width="50"
              :height="50"
              :src="record.cover"
            />
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space size="small">
              <a-button type="primary">
                编辑
              </a-button>
              <a-button type="danger">
                删除
              </a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import axios from "axios";
import {TableColumnsType} from "ant-design-vue";

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const ebooks = ref()
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    })
    const loading = ref(false)

    const columns = ref<TableColumnsType>([
      {
        title: '封面',
        dataIndex: 'cover',
        key: 'cover',
      },
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name'
      },
      {
        title: '分类一',
        dataIndex: 'category1Id',
        key: 'category1Id'
      },
      {
        title: '分类二',
        dataIndex: 'category2Id',
        key: 'category2Id'
      },
      {
        title: '文档数',
        dataIndex: 'docCount',
        key: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount',
        key: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount',
        key: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
      }
    ])

    /**
     * 数据查询
     */
    const handleQuery = (params: any) => {
      loading.value = true
      axios.get("/ebook/list", params).then((response) => {
        loading.value = false
        const data = response.data
        ebooks.value = data.content
      })

      // 重置分页按钮
      pagination.value.current = params.page
    }

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination)
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      })
    }

    onMounted(() => {
      handleQuery({})
    })

    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange
    };
  },
});
</script>