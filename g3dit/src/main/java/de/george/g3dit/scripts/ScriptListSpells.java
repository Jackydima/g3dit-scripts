package de.george.g3dit.scripts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamunify.i18n.I;

import de.george.g3dit.cache.Caches;
import de.george.g3utils.util.Pair;
import de.george.lrentnode.classes.desc.CD;
import de.george.lrentnode.classes.desc.CD.gCItem_PS;
import de.george.lrentnode.enums.G3Enums;
import de.george.lrentnode.iterator.TemplateFileIterator;
import de.george.lrentnode.properties.eCEntityProxy;
import de.george.lrentnode.template.TemplateEntity;
import de.george.lrentnode.template.TemplateFile;

public class ScriptListSpells implements IScript {

	@Override
	public String getTitle() {
		return I.tr("List spells");
	}

	@Override
	public String getDescription() {
		return I.tr("List all spells (that are used)");
	}

	@Override
	public boolean execute(IScriptEnvironment env) {
		List<Pair<TemplateEntity, String>> spellUsers = new ArrayList<>();
		Map<String, TemplateEntity> spells = new HashMap<>();

		TemplateFileIterator tpleFilesIterator = env.getFileManager().templateFilesIterator();
		while (tpleFilesIterator.hasNext()) {
			TemplateFile aFile = tpleFilesIterator.next();
			if (aFile.getHeaderCount() != 2) {
				continue;
			}

			TemplateEntity tple = aFile.getReferenceHeader();
			var spellGuid = tple.getPropertyNoThrow(CD.gCItem_PS.Spell).map(eCEntityProxy::getGuid).orElse(null);
			if (spellGuid != null) {
				spellUsers.add(Pair.of(tple, spellGuid));
			} else if (tple.hasClass(CD.gCMagic_PS.class)) {
				spells.put(tple.getGuid(), tple);
			}
		}

		var stringtable = Caches.stringtable(env.getEditorContext());
		for (var item : spellUsers) {
			var spellUser = item.el0();
			var spell = spells.get(item.el1());
			var spellUserName = stringtable.getFocusNamesOrEmpty().get(spellUser.getName());
			env.log(spellUser + "|" + spellUserName + "|" + spell.getName() + "|"
					+ G3Enums.asString(G3Enums.gEItemCategory.class, spellUser.getProperty(CD.gCItem_PS.Category).getEnumValue()) + "|"
					+ spellUser.getProperty(gCItem_PS.Texture).getString() + "|"
					+ G3Enums.asString(G3Enums.gESpellDeity.class, spell.getProperty(CD.gCMagic_PS.SpellDeity).getEnumValue()));
		}

		return true;
	}
}
