(ns graph-test.core
  (:require; [clojurewerkz.ogre.core :as q]
           ; [clojurewerkz.ogre.vertex :as v]
           ; [clojurewerkz.ogre.edge :as e]
           ; [clojurewerkz.ogre.element :as el]
            [clj-time.core :as t]
            [clj-time.coerce :as c])
  (:import [com.thinkaurelius.titan.core TitanFactory TitanGraph TitanTransaction]
           [java.util Date]
           [org.apache.tinkerpop.gremlin.process.traversal Traversal P]))

(defn pt [t l]
  (. t (as (name l))))

(comment (defn create-db []
   (let [ ;graph (doto (TitanFactory/build) (.set "storage.backend" "berkeleyje") (.set "storage.directory" "/tmp/graph") (.open))
         graph (TitanFactory/open "berkeleyje:/tmp/titan")
         ve1 (v/create! graph {:vurejdid 10001})
         a1 (v/create! graph {:adrid 11111 :vejnavn "brobergsgade" :nr 11 :by "kbh" :postnr 1427})
         p1 (v/create! graph {:key-name "bolig-areal" :val 100})
         p2 (v/create! graph {:key-name "grund-areal" :val 1000})
         p3 (v/create! graph {:key-name "bolig-areal" :val 120})
         p4 (v/create! graph {:key-name "grund-areal" :val 900})
         _ (e/connect! ve1 :param p1 {:tx (c/to-date (t/date-time 2016 01 4)) :fra (c/to-date (t/date-time 2010 10 14)) :til (c/to-date (t/date-time 2012 10 14))})
         _ (e/connect! ve1 :param p2 {:tx (c/to-date (t/date-time 2016 01 4)) :fra (c/to-date (t/date-time 2010 10 14)) :til (c/to-date (t/date-time 2013 10 14))})
         _ (e/connect! ve1 :param p3 {:tx (c/to-date (t/date-time 2016 01 11)) :fra (c/to-date (t/date-time 2012 10 15)) :til (c/to-date (t/date-time 2100 10 14))})
         _ (e/connect! ve1 :param p4 {:tx (c/to-date (t/date-time 2016 01 9)) :fra (c/to-date (t/date-time 2013 10 15)) :til (c/to-date (t/date-time 2100 10 14))})
         e1 (e/connect! ve1 :adresse a1 {:tx (c/to-date (t/date-time 2016 01 4)) :fra (c/to-date (t/date-time 2010 10 14)) :til (c/to-date (t/date-time 2100 10 14))})
         bfe1 (v/create! graph {:bfeid 2001})
         e2 (e/connect! ve1 :bfe bfe1 {:tx (c/to-date (t/date-time 2016 01 4)) :fra (c/to-date (t/date-time 2010 10 14)) :til (c/to-date (t/date-time 2100 10 14))})]
     (q/query (v/find-by-kv graph :vurejdid 10001)
                                        ;    (pt :a)
              (q/as "ve")
              (q/outE :param)
              (q/has :fra (P/lt (c/to-date (t/date-time 2014 10 13))))
              (q/has :til (P/gt (c/to-date (t/date-time 2014 10 13))))
              (q/has :tx  (P/lt (c/to-date (t/date-time 2016 01 12))))
              q/in-vertex
                                        ;  (q/as :p)
              q/properties
              q/into-vec!
                                        ; (q/values :bolig-areal)
                                        ;   q/path
                                        ;   q/next!
                                        ;    q/map-values
              ))))

(defn create-db []
  (let [;graph (TitanFactory/open "berkeleyje:/tmp/titan")
        graph (.. (TitanFactory/build) (set "storage.backend" "inmemory") (open))
        ve1 (. graph (addVertex (into-array Object ["vurejdid" 10001])))
        a1 (. graph (addVertex (into-array Object ["adrid" 11111 "vejnavn" "brobergsgade" "nr" 11 "by" "kbh" "postnr" 1427])))
        p1 (. graph (addVertex (into-array Object ["key-name" "bolig-areal" "val" 100])))
        p2 (. graph (addVertex (into-array Object ["key-name" "grund-areal" "val" 1000])))
        p3 (. graph (addVertex (into-array Object ["key-name" "bolig-areal" "val" 120])))
        p4 (. graph (addVertex (into-array Object ["key-name" "grund-areal" "val" 900])))
        bfe1 (. graph (addVertex (into-array Object ["bfeid" 2001])))
        _ (. ve1 (addEdge "param" p1 (into-array Object ["tx" (c/to-date (t/date-time 2016 01 4)) "fra" (c/to-date (t/date-time 2010 10 14)) "til" (c/to-date (t/date-time 2012 10 14))])))
        _ (. ve1 (addEdge "param" p1 (into-array Object ["tx" (c/to-date (t/date-time 2016 01 4)) "fra" (c/to-date (t/date-time 2010 10 14)) "til" (c/to-date (t/date-time 2013 10 14))])))
        _ (. ve1 (addEdge "param" p1 (into-array Object ["tx" (c/to-date (t/date-time 2016 01 11)) "fra" (c/to-date (t/date-time 2012 10 15)) "til" (c/to-date (t/date-time 2100 10 14))])))
        _ (. ve1 (addEdge "param" p1 (into-array Object ["tx" (c/to-date (t/date-time 2016 01 9)) "fra" (c/to-date (t/date-time 2013 10 15)) "til" (c/to-date (t/date-time 2100 10 14))])))
        _ (. ve1 (addEdge "adresse" a1 (into-array Object ["tx" (c/to-date (t/date-time 2016 01 4)) "fra" (c/to-date (t/date-time 2010 10 14)) "til" (c/to-date (t/date-time 2100 10 14))])))
        _ (. ve1 (addEdge "bfe" bfe1 (into-array Object ["tx" (c/to-date (t/date-time 2016 01 4)) "fra" (c/to-date (t/date-time 2010 10 14)) "til" (c/to-date (t/date-time 2100 10 14))])))
        g (.traversal graph)]
    g))

(defn q [g]
  (let [sn (.. g (V (into-array Object [])) (has "vurejdid" 10001) next)
        vertices (.. g (V (into-array Object [sn]))
                     (as "ve" (into-array String []))
                     (outE (into-array String ["param"]))
                     (has "fra" (P/lt (c/to-date (t/date-time 2014 10 13))))
                     (has "til" (P/gt (c/to-date (t/date-time 2014 10 13))))
                     (has "tx" (P/lt (c/to-date (t/date-time 2016 01 12))))
                     inV
                     (as "params" (into-array String []))
                     (select "ve" "params" (into-array String []))
                     iterate)]
                                        ;(map #(. % (properties (into-array String ["vurejdid"]))) (seq vertices))
    ;(map #(.getValue %) (flatten (map vec vertices)))
    ;(iterator-seq (. (get (first vertices) "params") (properties (into-array String []))))
    vertices))
