<template>
  <div class="dashboard">
    <!-- Botão Sair no topo -->
    <button class="logout-btn" @click="logout">Sair</button>

    <header>
      <h1>Seja bem-vindo, {{ userName }} 👋</h1>
      <p>O que você deseja fazer hoje? Comece criando suas tarefas.</p>
    </header>

    <section class="actions">
      <button class="primary" @click="goToCreateTasklist">Criar nova lista</button>
      <button class="outline" @click="goToProfile">Meu perfil</button>
    </section>

    <section class="lists">
      <h2>Suas listas</h2>

      <div v-if="lists.length === 0" class="empty">
        <p>Você ainda não criou nenhuma lista.</p>
        <p>Comece criando sua primeira tasklist 🚀</p>
      </div>

      <div v-else class="grid">
        <div
          v-for="list in lists"
          :key="list.id"
          class="card"
          :class="{ completed: list.completed }"
        >
          <h3>{{ list.name }}</h3>
          <p>{{ list.description }}</p>
          <div class="card-actions">
            <button class="edit" @click="editTasklist(list.id)">Editar</button>
            <button class="complete" @click="completeTasklist(list.id)" :disabled="list.completed">
              {{ list.completed ? 'Concluída' : 'Finalizar' }}
            </button>
            <button class="delete" @click="deleteTasklist(list.id)">Excluir</button>
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
  router.push({ name: 'createTasklist' })
}

function goToProfile() {
  router.push({ name: 'profile' })
}

async function fetchTasklists() {
  try {
    const { data } = await http.get<Tasklist[]>('/tasklists', {
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
    const { data } = await http.put<Tasklist>(
      `/tasklists/${id}/complete`,
      {},
      { headers: { Authorization: `Bearer ${authStore.token}` } },
    )
    lists.value = lists.value.map((l) => (l.id === id ? { ...l, completed: data.completed } : l))
  } catch (err) {
    console.error('Erro ao finalizar tasklist', err)
  }
}

async function deleteTasklist(id: string) {
  if (!confirm('Deseja realmente excluir esta lista?')) return
  try {
    await http.delete(`/tasklists/${id}`, {
      headers: { Authorization: `Bearer ${authStore.token}` },
    })
    lists.value = lists.value.filter((l) => l.id !== id)
  } catch (err) {
    console.error('Erro ao excluir tasklist', err)
  }
}

async function logout() {
  try {
    await http.post('/auth/logout', {}, { headers: { Authorization: `Bearer ${authStore.token}` } })
  } catch (err) {
    console.error('Erro no logout', err)
  } finally {
    authStore.clear()
    router.push({ name: 'home' })
  }
}
</script>

<style scoped>
.dashboard {
  padding: 40px;
  position: relative;
}

/* Botão Sair no topo */
.logout-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  background: #dc2626;
  color: white;
  padding: 12px 24px;
  font-weight: 600;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(220, 38, 38, 0.4);
}

.logout-btn:hover {
  background: #b91c1c;
  transform: scale(1.05);
  box-shadow: 0 6px 20px rgba(220, 38, 38, 0.6);
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
  align-items: center;
  gap: 16px;
  margin-bottom: 40px;
}

/* Botão Criar nova lista */
button.primary {
  background: #2563eb;
  color: white;
  padding: 12px 24px;
  font-weight: 600;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: all 0.3s ease;
}

button.primary:hover {
  background: #1d4ed8;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.4);
}

/* Botão Meu Perfil */
button.outline {
  border: 2px solid #2563eb;
  background: transparent;
  color: #2563eb;
  padding: 12px 24px;
  font-weight: 600;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

button.outline:hover {
  background: #2563eb;
  color: white;
  box-shadow: 0 4px 15px rgba(37, 99, 235, 0.4);
  transform: translateY(-2px);
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
  position: relative;
  padding: 20px;
  border-radius: 12px;
  background: white;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.2s;
}

.card.completed {
  background: #e5e7eb;
  color: #6b7280;
  text-decoration: line-through;
}

.card.completed::after {
  content: 'Finalizado';
  position: absolute;
  top: 10px;
  right: -40px;
  transform: rotate(45deg);
  background: #16a34a;
  color: white;
  padding: 2px 12px;
  font-size: 12px;
  font-weight: bold;
  border-radius: 4px;
}

.card-actions {
  display: flex;
  gap: 6px;
  margin-top: 12px;
}

.card-actions button {
  padding: 4px 10px;
  font-size: 12px;
  border-radius: 6px;
  font-weight: 600;
  cursor: pointer;
  border: none;
  transition: all 0.2s;
}

.card-actions button.edit {
  background: #2563eb;
  color: white;
}

.card-actions button.edit:hover {
  background: #1d4ed8;
}

.card-actions button.complete {
  background: #16a34a;
  color: white;
}

.card-actions button.complete:hover {
  background: #15803d;
}

.card-actions button.complete:disabled {
  background: #9ca3af;
  cursor: not-allowed;
}

.card-actions button.delete {
  background: #dc2626;
  color: white;
}

.card-actions button.delete:hover {
  background: #b91c1c;
}
</style>
