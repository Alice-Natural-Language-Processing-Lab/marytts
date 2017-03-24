package marytts.io;

import marytts.data.item.phonology.Phoneme;
import marytts.data.item.Item;
import marytts.data.Sequence;
import marytts.data.Relation;
import marytts.features.FeatureMap;
import marytts.features.Feature;
import marytts.data.Utterance;
import marytts.io.MaryIOException;
import marytts.data.SupportedSequenceType;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.io.File;


import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 *
 *
 * @author <a href="mailto:slemaguer@coli.uni-saarland.de">Sébastien Le Maguer</a>
 */
public class ROOTSJSONSerializer implements Serializer
{
    public ROOTSJSONSerializer()
    {
    }

    public Utterance load(File file)
        throws MaryIOException
    {
        throw new UnsupportedOperationException();
    }

    public void save(File file, Utterance utt)
        throws MaryIOException
    {
        throw new UnsupportedOperationException();
    }

    public String toString(Utterance utt)
        throws MaryIOException
    {
        String output = "{\n";
        output += "\t\"sequences\": {\n";

        SupportedSequenceType cur_type = null;
        Object[] types =  utt.listAvailableSequences().toArray();
        int t = 0;
        int i = 0;
        while (t < (types.length-1))
        {
            cur_type = ((SupportedSequenceType) types[t]);
            output += "\t\t\"" +  cur_type + "\": [\n";
            Sequence<Item> seq = (Sequence<Item>) utt.getSequence(cur_type);
            i = 0;
            while (i < (seq.size()-1))
            {
                output +="\t\t\t\"" + seq.get(i).toString() + "\",\n";
                i++;
            }
            output +="\t\t\t\"" + seq.get(i).toString() + "\"\n";
            output += "\t\t],\n";
            t++;
        }

        cur_type = ((SupportedSequenceType) types[t]);
        output += "\t\t\"" + cur_type + "\": [\n";
        output += "\t\t]\n";
        output += "\t},\n";

        output += "\t\"relations\": [\n";
        Object[] relations = utt.listAvailableRelations().toArray();
        ImmutablePair<SupportedSequenceType, SupportedSequenceType> cur_rel_id;
        SparseDoubleMatrix2D cur_rel;

        int r = 0;
        while (r < (relations.length-1))
        {
            output += "\t\t{\n";
            cur_rel_id = (ImmutablePair<SupportedSequenceType, SupportedSequenceType>) relations[r];
            output += "\t\t\t\"source\" : \"" + cur_rel_id.left + "\",\n";
            output += "\t\t\t\"target\" : \"" + cur_rel_id.right + "\",\n";


            cur_rel = utt.getRelation(cur_rel_id.left, cur_rel_id.right).getRelations();
            output += "\t\t\t \"matrix\" : [\n";
            for (int j=0; j<cur_rel.rows(); j++)
            {
                output += "\t\t\t\t";
                for (int k=0; k<cur_rel.columns(); k++)
                {
                    output += cur_rel.get(j, k) + " ";
                }
                output += "\n";
            }
            output += "\t\t\t]\n";


            output += "\t\t},\n";
            r++;
        }

        output += "\t\t{\n";
        cur_rel_id = (ImmutablePair<SupportedSequenceType, SupportedSequenceType>) relations[r];
        output += "\t\t\t\"source\" : \"" + cur_rel_id.left + "\",\n";
        output += "\t\t\t\"target\" : \"" + cur_rel_id.right + "\",\n";
        output += "\t\t},\n";

        output += "\t]\n";
        output += "}\n";
        return output;
    }

    public Utterance fromString(String content)
        throws MaryIOException
    {
        throw new UnsupportedOperationException();
    }

}


/* ROOTSJSONSerializer.java ends here */
