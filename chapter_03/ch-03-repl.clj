(use 'clojure.repl)

;; ------------ Week 8 -------------- ;;

(use 'clojure.set)
(union #{1 2} #{2 3})
(intersection #{1 2} #{2 3})
(difference #{1 2} #{2 3})

(def answer-annotations
(fn [courses registrants-courses]
(let [checking-set (set registrants-courses)] (map (fn [course]
                (assoc course
                       :spaces-left (- (:limit course)
                                       (:registered course))
                       :already-in? (contains? checking-set
                                               (:course-name course))))
courses))))

(answer-annotations [{:course-name "zigging" :limit 4,
                             :registered 3}
                            {:course-name "zagging" :limit 1,
                             :registered 1}]
                           ["zagging"])

(def domain-annotations (fn [courses]
(map (fn [course] (assoc course
                :empty? (zero? (:registered course))
                :full? (zero? (:spaces-left course))))
            courses)))

(domain-annotations [{:registered 1, :spaces-left 1},
                            {:registered 0, :spaces-left 1},
                            {:registered 1, :spaces-left 0}])

(def note-unavailability
(fn [courses instructor-count]
(let [out-of-instructors? (= instructor-count
(count (filter (fn [course] (not (:empty? course))) courses)))]
(map (fn [course] (assoc course
                       :unavailable? (or (:full? course)
                                         (and out-of-instructors?
                                              (:empty? course)))))
courses))))

(def annotate
(fn [courses registrants-courses instructor-count]
       (note-unavailability (domain-annotations
                             (answer-annotations courses
                                                 registrants-courses))
                            instructor-count)))

(def annotate
(fn [courses registrants-courses instructor-count]
(let [answers (answer-annotations courses registrants-courses)
             domain (domain-annotations answers)
             complete (note-unavailability domain
                                           instructor-count)]
complete)))

;; -- Exercise 1 ---
(defn devector [vector]
  (-> vector
      first
      inc
      list)
)

(devector [1])

;; -- Exercise 2 ---
(defn devector [vector]
  (-> vector
      first
      inc
      (* 3)
      list)
)

(devector [1])

;; -- Exercise 3 ---
(-> 3
    ((fn [n] (* 2 n)))
    inc)

;; -- Exercise 4 ---

(+ (* (+ 1 2) 3) 4)

(-> 1
    (+ 2)
    (* 3)
    (+ 4))
