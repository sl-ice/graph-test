(ns graph-test.core
  (:require [clojurewerkz.ogre.core :as q]
            [clojurewerkz.ogre.vertex :as v]
            [clojurewerkz.ogre.edge :as e]
            [clojurewerkz.ogre.element :as el])
  (:import [com.thinkaurelius.titan.core TitanFactory TitanGraph TitanTransaction]
           [java.util Date]))

(defn create-db []
  (let [;graph (doto (TitanFactory/build) (.set "storage.backend" "berkeleyje") (.set "storage.directory" "/tmp/graph") (.open))
        graph (TitanFactory/open "berkeleyje:/tmp/titan")
        ve1 (. graph (addVertex (into-array Object ["id" 101 "vurejdid" 10001])))
        a1 (. graph (addVertex (into-array Object ["id" 111 "adrid" 11111 "vejnavn" "brobergsgade"])))
        e1 (. ve1 (addEdge "adresse" a1 (into-array Object ["tx" (Date.) "fra" (Date.) "til" (Date.)])))
        ve2 (v/create! graph {:vurejdid 2001 :foo2 "bar"})]
    (prn (q/query (v/get-all-vertices graph)
              q/into-vec!))
    (q/query (v/find-by-kv graph "vurejdid" 10001)
             (q/--> "adresse")
             (q/values "vejnavn")
             q/path
             q/next!)))
