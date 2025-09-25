# 🚀 Concurrency Management Based on Logical Time in Multicore Environments for Situated Agent Applications

This Bachelor's thesis addresses the crucial problem of how to execute **Multi-Agent Systems (MAS)** in a parallel, efficient, and consistent manner on multicore architectures.

The main objective is to ensure the correct management of concurrent access to a shared resource, the **Territory**, avoiding the typical overhead of traditional lock mechanisms and achieving a significant **speed-up** in execution.

---

## 💡 The Key Concept: Synchronization with Logical Time

To minimize the performance overhead associated with conventional multi-threaded synchronization, the system adopts an innovative approach based on a notion of **composed or logical time** that replaces standard synchronization primitives.

---

## ⚙️ Conflict-free Number (CFN) Mechanism

The core of the methodology is the use of the **Conflict-free Number (CFN)**, a mechanism that establishes a partial order in operations, effectively preventing conflicts *a priori*.

* **Assignment:** Each agent acquires its own CFN from the specific cell of the territory where it is located.
* **Turn Management:** Agents are grouped by CFN. A system of turns is established based on the value of these labels.
* **Selective Parallelism:** This mechanism allows parallel execution only for agents belonging to the same group (identical CFN, no-conflict agents), ensuring sequential execution between groups with different CFNs (conflicting agents).
* **Result:** This achieves the atomicity and consistency of operations on the territory without the need for explicit locks or semaphores.

📷 *Illustration showing how agents are grouped within the territory based on their CFN labels.*

<img width="762" height="462" alt="Screenshot 2025-09-25 154249" src="https://github.com/user-attachments/assets/edeb4457-cb18-4bae-affd-14ad7890f5f3" />

---

## 🧩 Case Study: The Tileworld Environment

The practical efficacy and performance benefits of the CFN mechanism were robustly demonstrated through a case study based on a variant of the classic **Tileworld environment**.

Tileworld serves as a widely recognized testbed for situated agents, consisting of a grid populated by **tiles, holes**. In this simulation, multiple agents move concurrently with the goal of collecting tiles and subsequently using them to fill holes.

The CFN system is applied by assigning a unique Conflict-Free Number to each cell such that no two potentially conflicting agents (those within twice the agent’s action radius) are assigned the same number.

By leveraging the CFN to establish a partial order of execution, the system allows non-conflicting agents to execute in parallel, successfully guaranteeing **mutual exclusion** and **synchronization** within the shared environment, and thereby achieving significant performance gains over traditional lock-based concurrency control methods.

📷 *Screenshot of the Tileworld GUI, showing agents, resources, and holes with their respective labels and states.*

<img width="690" height="517" alt="Screenshot 2025-09-25 154324" src="https://github.com/user-attachments/assets/96becea5-edf7-451a-a7b4-08583828d1a7" />

---

## 🖥️ Tileworld GUI Explanation

* The **green numbers** indicate the sources, where the number represents the amount of resources currently available for extraction.
* The **red numbers** indicate the holes, where the number specifies the depth of the hole.
* The **blue squares** represent the agents without a tile (unloaded agents).
* The **green squares** represent the agents carrying a tile (loaded agents, ready to deposit in a hole).

---

## 📄 Read More

To discover more details, including the full implementation and the achieved results, please read the complete thesis here: [Tesi.pdf](https://github.com/user-attachments/files/22534262/Tesi.pdf)

