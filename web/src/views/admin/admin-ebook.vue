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
              <a-button type="primary" @click="edit">
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

  <a-modal
      v-model:visible="modalVisible"
      title="Title"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <p>test</p>
  </a-modal>
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
      pageSize: 4,
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
      axios.get("/ebook/list", {
        params: {
          page: params.page,
          size: params.size
        }
      }).then((response) => {
        loading.value = false
        const data = response.data
        ebooks.value = data.content.list

        // 重置分页按钮
        pagination.value.current = params.page
        pagination.value.total = data.content.total
      })
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

    // -------- 表单 --------
    // const modalText = ref<string>('Content of the modal');
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    // const showModal = () => {
    //   visible.value = true;
    // };

    const handleModalOk = () => {
      // modalText.value = 'The modal will be closed after two seconds';
      modalLoading.value = true;
      setTimeout(() => {
        modalVisible.value = false;
        modalLoading.value = false;
      }, 2000);
    };

    /**
     * 编辑
     */
    const edit = () => {
      modalVisible.value = true
    }

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      })
    })

    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      edit,
      modalVisible,
      modalLoading,
      handleModalOk
    };
  },
});
</script>
