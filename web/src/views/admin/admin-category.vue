<template>

  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form
            layout="inline"
            :model="param"
        >
          <a-form-item>
            <a-button
                type="primary"
                @click="handleQuery"
            >
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add">
              新增
            </a-button>
          </a-form-item>
        </a-form>

      </p>
      <a-table
          :columns="columns"
          :data-source="categorys"
          :loading="loading"
          :pagination="false"
      >
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
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <a-popconfirm
                  title="删除后不可恢复 · 确认删除？"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleDelete(record.id)"
              >
                <a-button type="danger">
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      v-model:visible="modalVisible"
      title="分类表单"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <p>
      <a-form :model="category" :label-col="{ span: 6 }">
        <a-form-item label="名称">
          <a-input v-model:value="category.name" />
        </a-form-item>
        <a-form-item label="父分类">
          <a-input v-model:value="category.parent" />
        </a-form-item>
        <a-form-item label="顺序">
          <a-input v-model:value="category.sort" />
        </a-form-item>
      </a-form>
    </p>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import axios from "axios";
import {TableColumnsType} from "ant-design-vue";
import { message } from "ant-design-vue";
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminCategory',
  setup() {
    const param = ref()
    param.value = {}
    const categorys = ref()
    const loading = ref(false)

    const columns = ref<TableColumnsType>([
      {
        title: '名称',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '父分类',
        dataIndex: 'parent',
        key: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort',
        key: 'sort'
      },
      {
        title: 'Action',
        key: 'action',
      }
    ])

    /**
     * 数据查询
     */
    const handleQuery = () => {
      loading.value = true
      axios.get("/category/all", ).then((response) => {
        loading.value = false
        const data = response.data
        if (data.success) {
          categorys.value = data.content
        } else {
          message.error(data.message)
        }
      })
    }

    // -------- 表单 --------
    const category = ref({});
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const handleModalOk = () => {
      modalLoading.value = true;

      axios.post("/category/save", category.value).then((response) => {
        modalLoading.value = false;
        const data = response.data
        if (data.success) {
          modalVisible.value = false;
          // 重新加载列表
          handleQuery()
        } else {
          message.error(data.message)
        }
      })
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true
      category.value = Tool.copy(record)
    }

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true
      category.value = {}
    }

    const handleDelete = (id: number) => {
      axios.delete("/category/delete/" + id).then((response) => {
        const data = response.data
        if (data.success) {
          // 重新加载列表
          handleQuery()
        }
      })
    }

    onMounted(() => {
      handleQuery()
    })

    return {
      param,
      categorys,
      columns,
      loading,

      edit,
      add,
      handleDelete,

      category,
      modalVisible,
      modalLoading,
      handleModalOk,
      handleQuery
    };
  },
});
</script>
