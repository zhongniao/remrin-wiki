<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          v-model:selectedKeys="selectedKeys2"
          v-model:openKeys="openKeys"
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <MainOutlined />
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template #title>
            <span><user-outlined />{{item.name}}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined /><span>{{child.name}}</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="isShowWelcome">
        <h1>欢迎使用REMRIN知识库</h1>
      </div>
      <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }" :data-source="ebooks">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
          <span v-for="{ type, text } in actions" :key="type">
            <component :is="type" style="margin-right: 8px" />
            {{ text }}
          </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar><a-avatar shape="square" :src="item.cover" :size="50" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue';
import {defineComponent, onMounted, reactive, ref, toRef} from 'vue'
import axios from 'axios'
import { Tool } from '@/util/tool';
import {message} from "ant-design-vue";

// const listData: Record<string, string>[] = [];
//
// for (let i = 0; i < 23; i++) {
//   listData.push({
//     href: 'https://www.antdv.com/',
//     title: `ant design vue part ${i}`,
//     avatar: 'https://joeschmoe.io/api/v1/random',
//     description:
//         'Ant Design, a design language for background applications, is refined by Ant UED Team.',
//     content:
//         'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
//   });
// }

export default defineComponent({
  name: 'HomeView',
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
  },
  setup() {
    const ebooks = ref()
    const ebooks1 = reactive({books: []})

    const level1 = ref();
    let categorys: any;
    /**
     * 查询所有分类
     */
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data
        if (data.success) {
          categorys = data.content
          console.log("原始数组：", categorys)

          level1.value = []
          level1.value = Tool.array2Tree(categorys, 0)
          console.log("树形结构：", level1.value)
        } else {
          message.error(data.message)
        }
      })
    }

    const isShowWelcome = ref(true)
    let categoryId2 = 0

    const handleQueryEbook = () => {
      axios.get("/ebook/list", {
        params: {
          page: 1,
          size: 1000,
          categoryId2: categoryId2
        }
      }).then((response) => {
        const data = response.data
        ebooks.value = data.content.list
        // ebooks1.books = data.content
      })
    }

    const handleClick = (value: any) => {
      // console.log("menu click")
      if (value.key === 'welcome') {
        isShowWelcome.value = true
      } else {
        categoryId2 = value.key
        isShowWelcome.value = false
        handleQueryEbook()
      }
    }

    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 3,
    };
    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ];

    onMounted(() => {
      handleQueryCategory()
      // handleQueryEbook()
    })

    return {
      ebooks,
      ebooks1: toRef(ebooks1, "books"),
      // listData,
      pagination,
      actions,
      handleClick,
      level1,

      isShowWelcome
    }
  }
});
</script>
