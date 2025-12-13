<template>
  <div class="tasklist-edit">
    <h2>Editar lista de tarefas</h2>

    <div v-if="loading" class="loading">Carregando...</div>
    <div v-else>
      <form @submit.prevent="handleUpdate">
        <div class="field">
          <label>Nome da lista</label>
          <input v-model="name" placeholder="Nome da lista" required />
        </div>

        <div class="field">
          <label>Descrição</label>
          <textarea v-model="description" placeholder="Descrição da lista" required></textarea>
        </div>

        <button type="submit" :disabled="loadingUpdate">
          {{ loadingUpdate ? 'Salvando...' : 'Salvar alterações' }}
        </button>
      </form>

      <button
        class="complete-btn"
        @click="handleComplete"
        :disabled="tasklist?.completed"
      >
        {{ tasklist?.completed ? 'Concluída' : 'Marcar como concluída' }}
      </button>

      <button class="delete-btn" @click="handleDelete">Excluir lista</button>

      <p v-if="error" class="error">{{ error }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import http from '@/api/http'
import { useAuthStore } from '@/stores/auth.store'
import { AxiosError } from 'axios'

interface Tasklist {
  id: string
  name: string
  description: string
  completed: boolean
  userId: string
}

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const tasklist = ref<Tasklist | null>(null)
const name = ref('')
const description = ref('')
const loading = ref(true)
const loadingUpdate = ref(false)
const error = ref<string | null>(null)

const tasklistId = route.params.id as string

// Buscar Tasklist ao montar
onMounted(async () => {
  try {
    const { data } = await http.get<Tasklist>(`/tasklists/${tasklistId}`, {
      headers: { Authorization: `Bearer ${authStore.token}` },
    })
    tasklist.value = data
    name.value = data.name
    description.value = data.description
  } catch (err: unknown) {
    if (err instanceof AxiosError) {
      error.value = err.response?.data?.message || 'Erro ao carregar a lista'
    } else if (err instanceof Error) {
      error.value = err.message
    } else {
      error.value = 'Erro desconhecido'
    }
  } finally {
    loading.value = false
  }
})

// Atualizar Tasklist
async function handleUpdate() {
  loadingUpdate.value = true
  error.value = null
  try {
    const { data } = await http.put<Tasklist>(
      `/tasklists/${tasklistId}`,
      { name: name.value, description: description.value },
      { headers: { Authorization: `Bearer ${authStore.token}` } }
    )
    tasklist.value = data
    alert('Lista atualizada com sucesso!')
  } catch (err: unknown) {
    if (err instanceof AxiosError) {
      error.value = err.response?.data?.message || 'Erro ao atualizar lista'
    } else if (err instanceof Error) {
      error.value = err.message
    } else {
      error.value = 'Erro desconhecido'
    }
  } finally {
    loadingUpdate.value = false
  }
}

// Finalizar Tasklist
async function handleComplete() {
  if (!tasklist.value || tasklist.value.completed) return
  try {
    const { data } = await http.put<Tasklist>(
      `/tasklists/${tasklistId}/complete`,
      {},
      { headers: { Authorization: `Bearer ${authStore.token}` } }
    )
    tasklist.value.completed = data.completed
  } catch {
    error.value = 'Erro ao marcar como concluída'
  }
}

// Excluir Tasklist
async function handleDelete() {
  if (!confirm('Deseja realmente excluir esta lista?')) return
  try {
    await http.delete(`/tasklists/${tasklistId}`, {
      headers: { Authorization: `Bearer ${authStore.token}` },
    })
    alert('Lista excluída com sucesso!')
    router.push({ name: 'dashboard' })
  } catch {
    error.value = 'Erro ao excluir lista'
  }
}
</script>

<style scoped>
.tasklist-edit {
  max-width: 500px;
  margin: 40px auto;
  padding: 20px;
  background: #f9fafb;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.05);
}

.tasklist-edit h2 {
  text-align: center;
  margin-bottom: 24px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 20px;
}

input, textarea {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border-radius: 6px;
  border: 1px solid #ccc;
  box-sizing: border-box;
}

textarea {
  min-height: 80px;
  resize: vertical;
}

button {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  margin-bottom: 10px;
  transition: background 0.2s;
}

button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.complete-btn {
  background: #16a34a;
  color: white;
}

.delete-btn {
  background: #dc2626;
  color: white;
}

.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}

.loading {
  text-align: center;
  font-weight: bold;
}
</style>
