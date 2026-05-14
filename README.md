# 🃏 Chinchón — Juego de cartas en java - Juego fin de curso

Un proyecto completo que implementa el juego de cartas **Chinchón** utilizando la **baraja española**, con jugadores humanos y máquina, detección automática de combinaciones, cálculo de puntuaciones y gestión completa de rondas.

---

## 📌 Características principales

- ✔️ Implementación completa del juego Chinchón  
- ✔️ Soporte para **jugadores humanos** y **jugadores máquina (IA)**  
- ✔️ IA con dos niveles de dificultad: **EASY** y **MEDIUM**  
- ✔️ Detección automática de:
  - Escaleras (Ladders)
  - Tríos (Triples)
  - Chinchón (7 cartas consecutivas del mismo palo)
- ✔️ Cálculo de puntuaciones por ronda y acumuladas  
- ✔️ Gestión de mazo, pila de descarte y manos de jugadores  
- ✔️ Consola interactiva para jugadores humanos  

---

## 🧠 Arquitectura del proyecto

El proyecto está organizado en torno a dos paquetes principales:

### `chinchon.dominio`
Contiene toda la lógica del juego:

| Clase | Descripción |
|-------|-------------|
| **Card** | Representa una carta de la baraja española. |
| **Suit** | Enum de palos: Coins, Cups, Swords, Clubs. |
| **Value** | Enum de valores: 1–12, junto ordenes 1 - 10 para hacer más fácil la gestión de combinaciones. |
| **Combination** | Agrupa cartas que forman una combinación válida. |
| **CombinationType** | Enum: LADDER, TRIPLE, CHINCHON. |
| **CombinationAnalyzer** | Motor principal que detecta combinaciones, calcula puntos y analiza jugadas. |
| **Deck** | Mazo de cartas, con soporte para 1 o más barajas. |
| **DiscardPile** | Pila de descarte. |
| **Hand** | Mano de un jugador. |
| **Player** | Clase abstracta base para jugadores. |
| **HumanPlayer** | Jugador humano con interacción por consola. |
| **MachinePlayer** | IA con dos niveles de dificultad. |
| **Difficulty** | Enum: EASY, MEDIUM. |
| **PlayerFactory** | Crea jugadores humanos o máquina. |

### `chinchon.app`
Contiene la lógica de interacción y flujo del juego:

| Clase | Descripción |
|-------|-------------|
| **ConsoleInput** | Encargada de imprimir por pantalla y recoger datos del usuario |
| **Main** | Crea los objetos y métodos para inicializar el juego, define los puntos para fin de partida |
| **GameManager** | Define jugadores, barajas, prepara el juego, lo inicializa y prepara las rondas |
| **Round** | Encargada de toda la gestión interna de la ronda, robar,descartar, finalizar y detectar chinchon |


---

## 🎮 Cómo funciona el juego

### 1. Inicio de la partida
- Se define la puntuación máxima del juego (50-300)
- Se crean los jugadores (humanos o IA).
- Se inicializa el mazo y la pila de descarte.
- Se reparten las cartas iniciales.

### 2. Turno de cada jugador
Cada jugador realiza:

1. **Robar una carta**  
   - Puede robar del mazo (Cartas mezcladas)
   - Puede robar de la pila de descarte (Se enseña la última carta descartada)

2. **Descartar una carta**  
   - Puede descartar una carta de su mano.
   - En caso de cumplir con las condiciones puede terminar la ronda y descartar una carta.

3. **Comprobación de Chinchón**  
   - Si un jugador forma un Chinchón, gana automáticamente.

- **Las condiciones para cerrrar la ronda son las siguientes:**s
    - Debe tener 6 o más cartas combinadas (Se encarga combinationAnalyzer).
    - No se puede cerrar la ronda en turno 1.
    - La puntuación que se obtendría por cerrar la ronda no puede superar el máximo de puntos definida en el inicio de la partida. 

### 3. Final de ronda
- Se calculan puntos según las cartas no combinadas.
- Si un jugador supera el límite permitido, termina la partida.
- Se inicia una nueva ronda.

### 4. Final de partida 
- En caso de que alguien supere el máximo de puntos, ganará el que menos puntos tenga
- En caso de que alguien haya obtenido un chinchón (7 cartas en escalera combinadas), gana automáticamente.

# 📚 Explicación de todas las clases

## Clases de chinchon.dominio

## 🂡 **Card**
Representa una carta individual de la baraja española.  
Contiene:
- Un **palo** (`Suit`)
- Un **valor** (`Value`)

Incluye `toString()` para mostrar la carta con su símbolo.

---

## 🃏 **Suit**
Enum que representa los **palos** de la baraja española:
- Coins (Oros) 🪙  
- Cups (Copas) 🍷  
- Swords (Espadas) ⚔️  
- Clubs (Bastos) 🦯  

Cada uno tiene un símbolo Unicode para mostrarlo en consola.

---

## 🔢 **Value**
Enum que representa los valores de la baraja:
- 1 a 7  
- 10 (Sota), 11 (Caballo), 12 (Rey)

Incluye:
- `value`: valor numérico real  
- `order`: orden lógico para detectar escaleras (1–10)

---

## 🧩 **Combination**
Representa una combinación válida de cartas:
- Escalera (LADDER)
- Trío (TRIPLE)
- Chinchón (CHINCHON)

Guarda:
- Tipo de combinación  
- Lista de cartas que la forman  

---

## 🧩 **CombinationType**
Enum con los tipos de combinación:
- LADDER  
- TRIPLE  
- CHINCHON  

Incluye un nombre legible para mostrar en consola.

---

## 🧠 **CombinationAnalyzer**
El **motor del juego**.  
Se encarga de:

### ✔️ Detectar combinaciones:
- Escaleras  
- Tríos  
- Chinchón  

### ✔️ Ajustar escaleras para favorecer tríos  

### ✔️ Calcular puntuaciones:
- Suma de cartas no combinadas  
- −10 si el jugador se queda sin cartas  

### ✔️ Determinar:
- Cartas protegidas  
- Cartas inseguras  
- Cartas necesarias para completar combinaciones  

### ✔️ Generar representaciones de:
- Combinaciones  
- Cartas no combinadas  

Es una clase **Singleton**.

---

## 🂠 **Deck**
Representa el **mazo** del juego.  
Permite:

- Crear 1 o más barajas españolas  
- Barajar  
- Robar cartas  
- Reiniciar el mazo  
- Obtener todas las cartas posibles  

---

## 🗑️ **DiscardPile**
Representa la **pila de descarte**.  
Permite:

- Añadir cartas  
- Robar la última carta  
- Ver la última carta  
- Vaciar la pila y devolver su contenido  

---

## ✋ **Hand**
Representa la **mano de un jugador**.  
Permite:

- Añadir cartas  
- Eliminar cartas  
- Ordenar la mano por palo y valor  
- Mostrar la mano enumerada  

---

## 🧍 **Player** (abstracta)
Clase base para todos los jugadores.  
Contiene:

- Nickname  
- Mano (`Hand`)  
- Puntos acumulados  
- Acceso al `CombinationAnalyzer`  
- Método abstracto `decisionMaking()`  

Incluye lógica común como:
- Añadir puntos  
- Obtener cartas descartables  

---

## 🧑 **HumanPlayer**
Extiende `Player`.  
Incluye interacción por consola:

- Elegir de dónde robar  
- Elegir qué carta descartar  
- Decidir si terminar la ronda  

Usa `ConsoleInput`.

---

## 🤖 **MachinePlayer**
Extiende `Player`.  
Implementa la IA del juego:

### Dificultad EASY:
- Roba aleatoriamente  
- Descarta aleatoriamente  
- Puede terminar la ronda si cumple condiciones  

### Dificultad MEDIUM:
- Analiza cartas protegidas  
- Busca cartas que completan combinaciones  
- Decide estratégicamente:
  - De dónde robar  
  - Qué descartar  
  - Cuándo terminar la ronda  

---

## 🎚️ **Difficulty**
Enum con los niveles de dificultad:
- EASY  
- MEDIUM  

---

## 🏭 **PlayerFactory**
Crea jugadores:
- Humanos  
- Máquina con dificultad  

Facilita la extensibilidad del proyecto.

---

## Clases de chinchon.app

## 🔄 **Round** 
Gestiona:

- Turnos  
- Robos y descartes  
- Comprobación de Chinchón  
- Finalización de ronda  
- Puntuaciones  

Es el núcleo del flujo de partida.

---

## ⌨️ **ConsoleInput** 
Maneja:
- Lectura segura de enteros  
- Lectura de texto  
- Impresión formateada  

Evita errores de entrada del usuario.

## 🕹️ **GameManager**
Se encarga de: 

### ✔️ Configuración inicial
- Solicita jugadores humanos  
- Crea jugadores máquina  
- Inicializa mazo, pila de descarte y ronda  

### ✔️ Gestión del estado global
- Lista de jugadores  
- Puntuación máxima  
- Mazo (`Deck`)  
- Pila de descarte (`DiscardPile`)  
- Ronda actual (`Round`)  

### ✔️ Control del flujo entre rondas
- Comprobación de puntuaciones  
- Reinicio de elementos  
- Determinación del ganador  

---

## 🚀 **Main**
Se encarga de: 

### ✔️ Inicializar el juego
- Crea una instancia de `GameManager`  
- Configura la puntuación máxima  
- Inicia la primera ronda  

### ✔️ Delegación total
Toda la lógica del juego se delega en `GameManager`, manteniendo `Main` simple y limpio.

### ✔️ Finalización
- Muestra el ganador  
- Termina la ejecución  

En otras palabras:  
**`Main` arranca el juego y deja que `GameManager` haga el resto.**

---

