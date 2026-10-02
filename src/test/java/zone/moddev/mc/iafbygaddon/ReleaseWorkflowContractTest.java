package zone.moddev.mc.iafbygaddon;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;

/** Keeps the release contract separate from the furniture catalog checks. */
public class ReleaseWorkflowContractTest {
    private static String workflow() throws Exception {
        return new String(Files.readAllBytes(Paths.get(".github/workflows/publish-release.yml")),
                StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Test public void publishingRequiresTheApprovedProjectAndBothParents() throws Exception {
        String text = workflow();
        assertTrue(text.contains("MinecraftModDevelopmentMods/IronAgeFurniture-OhTheBiomes-Addon"));
        assertTrue(text.contains("\"$curseforge_project_id\" != \"1707176\""));
        assertTrue(text.contains("ironagefurniture,oh-the-biomes-youll-go"));
        assertFalse(text.contains("iron-age-furniture,oh-the-biomes-youll-go"));
        assertTrue(text.contains("${project_slug}(required)"));
        assertTrue(text.contains("ironagefurniture) parent_project_id=341834"));
        assertTrue(text.contains("oh-the-biomes-youll-go) parent_project_id=247560"));
        assertTrue(text.contains("{curseforge:${parent_project_id}}"));
        assertTrue(text.contains("id: curseforge-upload"));
        assertTrue(text.contains("steps.curseforge-upload.outputs.curseforge-version"));
        assertTrue(text.contains("projectID:341834"));
        assertTrue(text.contains("projectID:247560"));
        assertFalse(text.contains("projectID:\""));
        assertTrue(text.contains("CurseForge rejected the required parent relations:"));
        assertTrue(text.contains("files/$FILE_ID/dependencies"));
        assertTrue(text.contains("sort_by(.id) == ["));
        assertTrue(text.contains("The public file data does not contain both required parent projects"));
        assertFalse(text.contains("(optional)"));
    }

    @Test public void dependencyRepairCannotReplaceAnUploadedArtifact() throws Exception {
        String text = new String(Files.readAllBytes(Paths.get(".github/workflows/update-file-dependencies.yml")),
                StandardCharsets.UTF_8);
        assertTrue(text.contains("projects/1707176/update-file"));
        assertTrue(text.contains("projectID:341834"));
        assertTrue(text.contains("projectID:247560"));
        assertFalse(text.contains("projectID:\""));
        assertTrue(text.contains("sha256sum --check"));
        assertTrue(text.contains("printf -v file_tail '%03d'"));
        assertTrue(text.contains("CurseForge file does not match the immutable GitHub release"));
        assertTrue(text.contains("CurseForge rejected the dependency update:"));
        assertTrue(text.contains("files/$FILE_ID/dependencies"));
        assertTrue(text.contains("sort_by(.id) == ["));
        assertTrue(text.contains("The public file data does not contain both required parent projects"));
        assertFalse(text.contains("/upload-file"));
        assertFalse(text.contains("create_release_tag"));
    }

    @Test public void publicationUsesOneBundleAfterAllChecksAndCredentialConfirmation() throws Exception {
        String text = workflow();
        for (String check : new String[] {"Build, test, and audit", "Cold bootstrap and reproducibility",
                "Analyze Java", "validation"}) assertTrue(text.contains("'" + check + "'"));
        assertTrue(text.contains("confirm_live_publication:"));
        assertTrue(text.contains("Required release secret is unavailable"));
        assertTrue(text.contains("      - release_confirmation\n"));
        assertTrue(text.contains("      - create_release_tag\n"));
        assertTrue(text.contains("      - publish_maven\n"));
        assertTrue(text.contains("      - publish_curseforge\n"));
        assertTrue(text.contains("sha256sum --check SHA256SUMS"));
        assertTrue(text.contains("-PpreparedReleaseDir=\"$RUNNER_TEMP/release-input\""));
        assertTrue(text.contains("ref: ${{ needs.preflight.outputs.release_sha }}"));
        assertTrue(text.contains("Existing tag $RELEASE_TAG resolves to $tag_sha instead of $RELEASE_SHA"));
        assertTrue(text.contains("cancel-in-progress: false"));
        assertFalse(text.contains("--force"));
    }
}
