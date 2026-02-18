# Saving App - Aplicación de Gestión de Ahorros

## Descripción

Saving App es una aplicación Android para gestionar tus metas de ahorro mensuales y hacer seguimiento de tus gastos diarios. Te permite establecer un presupuesto para cada mes del año y registrar tus gastos diarios para verificar si estás cumpliendo tus objetivos.

## Características

### 🎯 Gestión de Metas Mensuales
- Crear metas de ahorro personalizadas para cada mes
- Asignar nombre, descripción, monto objetivo e icono a cada meta
- Editar y eliminar metas existentes
- Visualización de progreso con barra de progreso y porcentajes
- Código de colores:
  - Verde: Dentro del presupuesto (0-79%)
  - Naranja: Cerca del límite (80-99%)
  - Rojo: Sobre presupuesto (100%+)

### 💸 Registro de Gastos Diarios
- Agregar gastos con nombre, descripción, monto y categoría
- Asignar gastos a días específicos del mes
- Editar y eliminar gastos registrados
- Cálculo automático de totales y saldo restante

### 📊 Panel de Control
- Visualización en tiempo real del progreso de cada meta
- Comparación entre presupuesto, gastado y disponible
- Indicadores visuales de estado (cumpliendo/excedido)
- Lista completa de gastos por mes

## Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Base de Datos**: Room Database
- **UI**: Material Design 3
- **Componentes**: 
  - LiveData para observación de datos reactiva
  - Coroutines para operaciones asíncronas
  - ViewBinding para binding de vistas seguro
  - RecyclerView para listas eficientes

## Estructura del Proyecto

```
app/src/main/java/com/savingapp/
├── data/
│   ├── dao/              # Data Access Objects
│   │   ├── MonthlyGoalDao.kt
│   │   └── DailyExpenseDao.kt
│   ├── database/        # Base de datos Room
│   │   └── SavingDatabase.kt
│   ├── model/           # Modelos de datos
│   │   ├── MonthlyGoal.kt
│   │   ├── DailyExpense.kt
│   │   └── GoalWithExpenses.kt
│   └── repository/      # Capa de repositorio
│       └── SavingRepository.kt
└── ui/
    ├── adapter/         # Adaptadores RecyclerView
    │   ├── GoalAdapter.kt
    │   └── ExpenseAdapter.kt
    ├── viewmodel/       # ViewModels
    │   └── SavingViewModel.kt
    ├── MainActivity.kt
    ├── AddEditGoalActivity.kt
    ├── ExpenseActivity.kt
    └── AddEditExpenseActivity.kt
```

## Requisitos del Sistema

- Android 7.0 (API 24) o superior
- Gradle 8.2+
- Kotlin 1.9.20+

## Cómo Compilar

1. Clona el repositorio:
   ```bash
   git clone https://github.com/FelixTellus/Saving-App.git
   ```

2. Abre el proyecto en Android Studio

3. Sincroniza el proyecto con Gradle

4. Ejecuta la aplicación en un emulador o dispositivo físico

## Uso de la Aplicación
### Crear una Meta Mensual
1. Toca el botón "+" en la pantalla principal
2. Completa el formulario:
   - Nombre de la meta
   - Descripción
   - Monto objetivo
   - Selecciona un icono
   - Mes y año
3. Toca "Guardar"

### Registrar un Gasto
1. Toca sobre una meta en la lista
2. En la pantalla de gastos, toca el botón "+"
3. Completa el formulario:
   - Nombre del gasto
   - Descripción
   - Monto
   - Categoría
   - Día del mes
4. Toca "Guardar"

### Editar o Eliminar
- Usa los botones de editar/eliminar en cada tarjeta
- Los gastos se eliminan automáticamente al eliminar una meta

## Funcionalidades Futuras

- 📊 Gráficos y estadísticas avanzadas
- 📅 Vista de calendario para gastos
- 📤 Exportación de datos a CSV/PDF
- 🔔 Notificaciones de presupuesto
- 🎨 Temas personalizables
- ☁️ Sincronización en la nube

## Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## Contacto

Desarrollado por FelixTellus

Repositorio: [https://github.com/FelixTellus/Saving-App](https://github.com/FelixTellus/Saving-App)