<template>
  <div class="tasklist-create">
    <h2>Criar nova lista de tarefas</h2>

    <form @submit.prevent="handleCreate">
      <div class="field">
        <input v-model="name" placeholder="Nome da lista" required />
        <textarea v-model="description" placeholder="Descrição da lista" required></textarea>
      </div>

      <button type="submit" :disabled="loading">
        {{ loading ? 'Criando...' : 'Criar' }}
      </button>

      <p v-if="error" class="error">{{ error }}</p>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import http from '@/api/http' // usa o arquivo de configuração axios
import { useAuthStore } from '@/stores/auth.store'
import { AxiosError } from 'axios'

const name = ref('')
const description = ref('')
const loading = ref(false)
const error = ref<string | null>(null)

const authStore = useAuthStore()
const router = useRouter()

async function handleCreate() {
  loading.value = true
  error.value = null

  try {
    // Aqui não repetimos /api/v1 porque já está no baseURL do http
    await http.post(
      '/tasklists',
      { name: name.value, description: description.value },
      { headers: { Authorization: `Bearer ${authStore.token}` } }
    )

    // Redireciona para dashboard
    router.push({ name: 'dashboard' })
  } catch (err: unknown) {
    // Tratamento de erro sem usar any
    if (err instanceof AxiosError) {
      error.value = err.response?.data?.message || 'Erro ao criar lista'
    } else if (err instanceof Error) {
      error.value = err.message
    } else {
      error.value = 'Erro desconhecido'
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.tasklist-create {
  max-width: 500px;
  margin: 40px auto;
  padding: 20px;
  background: #f9fafb;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.05);
}

.tasklist-create h2 {
  text-align: center;
  margin-bottom: 24px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 12px;
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
  background: #2563eb;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.2s;
}

button:disabled {
  background: #93c5fd;
  cursor: not-allowed;
}

.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}
</style>
