<template>
  <div class="tasks">
    <h1>Minhas Tarefas</h1>

    <ul>
      <li v-for="task in tasks" :key="task.id">
        {{ task.title }}
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface Task {
  id: number
  title: string
}

const tasks = ref<Task[]>([
  { id: 1, title: 'Estudar Vue 3' },
  { id: 2, title: 'Integrar backend Spring Boot' },
])
</script>

<style scoped>
.tasks {
  max-width: 600px;
  margin: 40px auto;
}

ul {
  padding: 0;
  list-style: none;
}

li {
  background: #f5f5f5;
  margin-bottom: 8px;
  padding: 12px;
  border-radius: 4px;
}
</style>
