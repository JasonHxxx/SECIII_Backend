package team.software.collect.similarity.textSimilarity.similarity.sentence.editdistance;

public class CharEditUnit extends EditUnit {

	private String content = "";

	public CharEditUnit(Character ch) {
		content = ch.toString();
	}

	@Override
	public String getUnitString() {
		return content;
	}

}
