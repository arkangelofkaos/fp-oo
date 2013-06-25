(use 'clojure.repl)

;; ------------ Week 8 -------------- ;;

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


(def inc5 (fn [x] (+ 5 x)))
(inc5 3)

(def make-incrementer
  (fn [increment]
    (fn [x] (+ increment x))))

(def inc5 (make-incrementer 5))
(inc5 3)

(def add3 (partial + 3))
(add3 8)


;; --- Exercise 1 ---
(def map-plus-2 (partial map (partial + 2)))

;; --- Exercise 2 ---

(doc juxt)
( (juxt empty? reverse count)
  [:a :b :c])

(def separate
(fn [pred sequence]
       [(filter pred sequence) (remove pred sequence)]))

(separate even? `(1 2 2 3 4))

(defn my-seperate [pred sequence]
  (let [predFilter (partial filter pred)
        complementPredFilter (partial filter (complement pred))]
   (juxt predFilter complementPredFilter)
         sequence)
)

(my-seperate even? `(1 2 2 3 4))

;; -- Exercise 3/4 ---

(def myfun
  (let [x 3]
    (fn [] x)))

(myfun)

(defn my-fun-helper [x]
  (fn [] x))

;; todo...

;; --- Exercise 5 ---

(def my-atom (atom 0))

(swap! my-atom inc)

(deref my-atom)

(doc swap!)

(swap! my-atom (defn myfunc [x] 33))
(deref my-atom)

;; --- Exercise 6 ---

(defn always [x]
  (defn _always [& args] x))

((always 8) 1 'a :foo)

(doc constantly)

;; --- Other exercises todo.... ----

(-> (+ 1 2) (* 3) (+ 4))

;; --- Week 11???? ---

;; Exercise 1

(let [a (concat '(a b c) '(d e f))
      b (count a)]
  (odd? b))

(-> `(a b c)
    (concat `(d e f))
    count
    odd?
)

(-> `(a b c)
    ((fn [step-1-value]
       (-> (concat step-1-value `(d e f))
           ((fn [step-2-value]
              (-> (count step-2-value)
                  ((fn [step-3-value]
                     (odd? step-3-value)
                  ))
              )
           ))
       )
    ))
 )


;; Exercise 2
(odd? (count (concat '(a b c) '(d e f))))

(-> `(a b c)
    (concat `(d e f))
    count
    odd?
)

(-> `(a b c)
    ((fn [step-1-value]
       (-> (concat step-1-value `(d e f))
           ((fn [step-2-value]
              (-> (count step-2-value)
                  ((fn [step-3-value]
                     (odd? step-3-value)
                  ))
              )
           ))
       )
    ))
)

;; Exercise 3

(-> 3
    (+ 2)
    inc)

(-> 3
    ((fn [step-1-value]
       (-> (+ 2 step-1-value)
           ((fn [step-2-value]
              (inc step-2-value)
           ))
       )
    ))
)








