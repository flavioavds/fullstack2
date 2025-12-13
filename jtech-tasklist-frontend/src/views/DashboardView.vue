<template>
  <div class="dashboard">
    <header>
      <h1>Seja bem-vindo, {{ userName }} 👋</h1>
      <p>O que você deseja fazer hoje? Comece criando suas tarefas.</p>
    </header>

    <section class="actions">
      <button class="primary" @click="goToCreateTasklist">Criar nova lista de tarefas</button>
      <button class="outline" @click="goToProfile">Meu perfil</button>
    </section>

    <section class="lists">
      <h2>Suas listas</h2>

      <div v-if="lists.length === 0" class="empty">
        <p>Você ainda não criou nenhuma lista.</p>
        <p>Comece criando sua primeira tasklist 🚀</p>
      </div>

      <div v-else class="grid">
        <div v-for="list in lists" :key="list.id" class="card">
          <h3>{{ list.name }}</h3>
          <p>{{ list.description }}</p>
          <div class="actions">
            <button @click="editTasklist(list.id)">Editar</button>
            <button @click="completeTasklist(list.id)">Finalizar</button>
            <button @click="deleteTasklist(list.id)">Excluir</button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'
import type { Tasklist } from '@/models/tasklist.model'
import http from '@/api/http'

const router = useRouter()
const authStore = useAuthStore()

const userName = computed(() => authStore.user?.name || 'Usuário')

const lists = ref<Tasklist[]>([])

function goToCreateTasklist() {
  router.push({ name: 'createTasklist' }) // rota que vamos criar depois
}

function goToProfile() {
  router.push({ name: 'profile' }) // rota que vamos criar depois
}

async function fetchTasklists() {
  try {
    const { data } = await http.get('/tasklists', {
      headers: { Authorization: `Bearer ${authStore.token}` },
    })
    lists.value = data
  } catch (err) {
    console.error('Erro ao buscar tasklists', err)
  }
}

onMounted(() => {
  if (!authStore.user) {
    authStore.fetchMe().then(fetchTasklists)
  } else {
    fetchTasklists()
  }
})

function editTasklist(id: string) {
  router.push({ name: 'editTasklist', params: { id } })
}

async function completeTasklist(id: string) {
  try {
    await http.put(
      `/api/v1/tasklists/${id}/complete`,
      {},
      { headers: { Authorization: `Bearer ${authStore.token}` } }
    )
    lists.value = lists.value.map(l =>
      l.id === id ? { ...l, completed: true } : l
    )
  } catch (err) {
    console.error('Erro ao finalizar tasklist', err)
  }
}

async function deleteTasklist(id: string) {
  try {
    await http.delete(`/api/v1/tasklists/${id}`, {
      headers: { Authorization: `Bearer ${authStore.token}` },
    })
    lists.value = lists.value.filter(l => l.id !== id)
  } catch (err) {
    console.error('Erro ao excluir tasklist', err)
  }
}

</script>

<style scoped>
.dashboard {
  padding: 40px;
}

header h1 {
  font-size: 32px;
  margin-bottom: 4px;
}

header p {
  color: #6b7280;
  margin-bottom: 24px;
}

.actions {
  display: flex;
  gap: 16px;
  margin-bottom: 40px;
}

button.primary {
  background: #2563eb;
  color: white;
  padding: 12px 24px;
  font-weight: 600;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: background 0.2s;
}

button.primary:hover {
  background: #1d4ed8;
}

button.outline {
  border: 2px solid #2563eb;
  background: transparent;
  color: #2563eb;
  padding: 12px 24px;
  font-weight: 600;
  border-radius: 8px;
  cursor: pointer;
  transition:
    background 0.2s,
    border-color 0.2s;
}

button.outline:hover {
  background-color: #e5e7eb;
  border-color: #1d4ed8;
}

.lists h2 {
  margin-bottom: 16px;
}

.empty {
  padding: 40px;
  background: #f9fafb;
  border-radius: 12px;
  text-align: center;
  color: #6b7280;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
}

.card {
  padding: 20px;
  border-radius: 12px;
  background: white;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08);
}
</style>
