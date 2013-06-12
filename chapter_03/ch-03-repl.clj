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

(def separate
(fn [predicate sequence]
[(filter predicate sequence) (remove predicate sequence)]))

;;(separate :already-in? courses)

(def visible-courses
  (fn [courses]
    (let
      [
       [guaranteed possibles]
       (separate :already-in? courses)
      ]
      (concat guaranteed
              (remove :unavailable? possibles)))))

(def final-shape
  (fn [courses]
    (let
      [desired-keys
       [:course-name :morning? :registered :spaces-left :already-in?]
      ]
      (map
       (fn [course]
         (select-keys course desired-keys)
       )
       courses
      )
    )
  )
)

(def half-day-solution
  (fn [courses registrants-courses instructor-count]
    (-> courses
        (annotate registrants-courses instructor-count) visible-courses
        ((fn [courses] (sort-by :course-name courses))) final-shape
    )
  )
)

(def solution
  (fn [courses registrants-courses instructor-count]
    (map (fn [courses]
           (half-day-solution courses registrants-courses
                                         instructor-count)
         )
         (separate :morning? courses)
     )
  )
)

;;; Exercise 1

{:manager? true
 :taking-now ["zig" "zag"]}

(def note-unavailability
     (fn [courses instructor-count manager?]
       (let [out-of-instructors?
             (= instructor-count
                (count (filter (fn [course] (not (:empty? course)))
                               courses)))]
         (map (fn [course]
                (assoc course
                       :unavailable? (or (:full? course)
                                         (and out-of-instructors?
                                              (:empty? course))
                                         (and manager?                     ;; <<===
                                              (not (:morning? course))))))
              courses))))

(def annotate
     (fn [courses registrant instructor-count]
       (-> courses
           (answer-annotations (:taking-now registrant))
           domain-annotations
           (note-unavailability instructor-count (:manager? registrant)))))

(def half-day-solution
     (fn [courses registrant instructor-count]
       (-> courses
           (annotate registrant instructor-count)
           visible-courses
           ((fn [courses] (sort-by :course-name courses)))
           final-shape)))

(def solution
     (fn [courses registrant instructor-count]
       (map (fn [courses]
              (half-day-solution courses registrant instructor-count))
            (separate :morning? courses))))



;;; Exercise 2

{:manager? true
 :taking-now ["zig" "zag"]
 :previously-taken ["updating"]}

{:morning? true
 ; ...
 :prerequisites ["zigging"]}

(def note-unavailability
     (fn [courses instructor-count registrant]
       (let [out-of-instructors?
             (= instructor-count
                (count (filter (fn [course] (not (:empty? course)))
                               courses)))]
         (map (fn [course]
                (assoc course
                       :unavailable? (or (:full? course)
                                         (and out-of-instructors?
                                              (:empty? course))
                                         (and (:manager? registrant)
                                              (not (:morning? course)))
                                         (not (superset? (set (:previously-taken registrant))
                                                         (set (:prerequisites course)))))))
              courses))))

(def annotate
     (fn [courses registrant instructor-count]
       (-> courses
           (answer-annotations (:taking-now registrant))
           domain-annotations
           (note-unavailability instructor-count registrant))))






