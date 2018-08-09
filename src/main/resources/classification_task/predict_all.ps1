cd "C:\Users\Marlin\Documents\Development\Web\darpa\data\Node2Vec"
python .\create_trainset_all.py
python .\train_cat_30.py
For ($i=32; $i -lt 33; $i++) {
    echo $i
    python .\train_cat_n.py $i
    python .\test_n.py $i + 1
    python .\edge_classification_in_sequence_visual.py $i
    
    }