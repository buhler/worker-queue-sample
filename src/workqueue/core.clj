(ns workqueue.core)
; another option to try to cancel running jobs after timeout - ScheduledExecutorService

(import 'java.util.concurrent.Executors)
(import '(java.util.concurrent LinkedBlockingQueue))

(def keep-going? (atom true))
(def total-job-count (atom 0))
(def resources-queue (LinkedBlockingQueue. ))
(def job-queue (LinkedBlockingQueue. ))
(def max-job-threads 10)
(def executor (Executors/newFixedThreadPool max-job-threads))

(defn timestamp []
  (quot (System/currentTimeMillis) 1000))

(defn diff-timestamp [start-time]
 (-  (timestamp) start-time))

(defn consumer [queue, handler] 
(future (while @keep-going? 
  (when-let [item (.take queue)]
  (handler item)
  (Thread/sleep 100)))))

(defn new-job [item]
 (hash-map :jobid (swap! total-job-count inc) 
           :item item 
           :start-time (timestamp)))

(defn job-complete [job]
  (println (str "job completed in " (diff-timestamp (:start-time job))
 " seconds ("(:item job)")"))
  (Thread/sleep 1000)
  (.put resources-queue (:item job)))

(defn job [] 
  (let [job (.poll job-queue)]
  (println "starting job\t" (:jobid job) "\t" (:item job))
  (Thread/sleep (+ 5000 (rand-int 25000)))
  (job-complete job)))

(defn resource-available [item]
 ;(println "resource available: "  item)
 (.put job-queue (new-job item))
 (.submit executor job))

(defn -main []
 (consumer resources-queue, resource-available)
 (doto resources-queue
     (.put "r1")
     (.put "r2")
     (.put "r3")
     (.put "r4")))

