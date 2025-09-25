🚀 Concurrency Management Based on Logical Time in Multicore Environments for Situated Agent Applications
This Bachelor's thesis addresses the crucial problem of how to execute Multi-Agent Systems (MAS) in a parallel, efficient, and consistent manner on modern multicore architectures.

The main objective is to ensure the correct management of concurrent access to a shared resource, the Territory, avoiding the typical overhead of traditional lock mechanisms and achieving a significant speed-up in execution.

💡 The Key Concept: Synchronization with Logical Time
To minimize the performance overhead associated with conventional multi-threaded synchronization, the system adopts an innovative approach based on a notion of composed or logical time that replaces standard synchronization primitives.

Conflict-free Number (CFN) Mechanism
The core of the methodology is the use of the Conflict-free Number (CFN), a mechanism that establishes a partial order in operations, effectively preventing conflicts a priori.

Assignment: Each agent acquires its own CFN from the specific cell of the territory where it is located.

Turn Management: Agents are grouped by CFN. A system of turns is established based on the value of these labels.

Selective Parallelism: This mechanism allows parallel execution only for agents belonging to the same group (identical CFN), ensuring sequential execution between groups with different CFNs.

Result: This achieves the atomicity and consistency of operations on the territory without the need for explicit locks or semaphores.

For implementation details, please read: [Tesi.pdf](https://github.com/user-attachments/files/22534262/Tesi.pdf)
