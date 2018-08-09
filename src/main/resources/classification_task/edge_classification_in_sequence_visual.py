import weka.core.jvm as jvm
from weka.classifiers import Classifier, Evaluation
from weka.core.converters import Loader
from weka.core.classes import Random
import os
import sys
import traceback


def main(fileId):
    # data_dir = 'C:/Users/ShimaKhoshraftar/Downloads/PhD/edge classification/Sta-emb/'
    data_dir = '../Node2Vec/Data/'

    fw2 = open(data_dir + 'precision_recall_KNN3.txt', 'w')
    fw2.write('precision,recall,f-score\n')

    fw = open(data_dir + '/predicted/graph_' + str(fileId), 'w')

    #####################run weka on test,train set####################
    # load data
    loader = Loader(classname="weka.core.converters.ArffLoader")
    trainSet = loader.load_file(data_dir + 'train.txt')
    trainSet.class_is_last()

    testSet = loader.load_file(data_dir + 'test.txt')
    testSet.class_is_last()

    # build classifier
    # KNN
    cls = Classifier(classname="weka.classifiers.lazy.IBk")
    cls.options = ["-K", "7", "-W", "0", "-A",
                   "weka.core.neighboursearch.LinearNNSearch -A \"weka.core.EuclideanDistance -R first-last\""]
    # cls.options = ["-K", "2", "-W", "0", "-A",
    #               "weka.core.neighboursearch.LinearNNSearch -A \"weka.core.EuclideanDistance -R first-last\""]

    # logistic regression
    # cls = Classifier(classname="weka.classifiers.functions.Logistic")
    # cls.options = ["-R", "1e-8", "-M", "-1", "-num-decimal-places","4"]

    # Random Forest
    # cls = Classifier(classname="weka.classifiers.trees.RandomForest")
    # cls.options = ["-P" ,"100" ,"-I" ,"100" ,"-num-slots" ,"1" ,"-K","0" ,"-M" ,"1.0" ,"-V" ,"0.001" ,"-S" ,"1"]

    cls.build_classifier(trainSet)
    evl = Evaluation(testSet)
    evl.test_model(cls, testSet)
    fw2.write(str(evl.precision(0)) + ',' + str(evl.recall(0)) + ',' + str(evl.f_measure(0)) + '\n')

    # print(evl.summary())
    # print(evl.class_details())
    print("precision:" + str(evl.precision(0)))
    print("recall:" + str(evl.recall(0)))
    print("fscore:" + str(evl.f_measure(0)))

    fw.write("# - actual - predicted - error - distribution\n")
    for index, inst in enumerate(testSet):
        pred = cls.classify_instance(inst)
        dist = cls.distribution_for_instance(inst)
        fw.write(
            "%d - %s - %s - %s  - %s \n" %
            (index + 1,
             inst.get_string_value(inst.class_index),
             inst.class_attribute.value(int(pred)),
             "yes" if pred != inst.get_value(inst.class_index) else "no",
             str(dist.tolist())))
    fw.close()

    fw2.close()


if __name__ == "__main__":
    try:
        jvm.start()
        main(sys.argv[1])
    except Exception as e:
        print(traceback.format_exc())
    finally:
        jvm.stop()
