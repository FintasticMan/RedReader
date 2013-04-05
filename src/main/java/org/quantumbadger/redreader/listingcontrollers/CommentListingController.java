/*******************************************************************************
 * This file is part of RedReader.
 *
 * RedReader is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedReader is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RedReader.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package org.quantumbadger.redreader.listingcontrollers;

import android.net.Uri;
import org.quantumbadger.redreader.cache.CacheRequest;
import org.quantumbadger.redreader.common.Constants;
import org.quantumbadger.redreader.fragments.CommentListingFragment;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

// TODO add notification/header for abnormal sort order
// TODO parse URLs, support parents/context -- use post permalink
public class CommentListingController {

	private final String postId;
	private final Uri uri;
	private UUID session = null;
	private Sort sort = Sort.BEST; // TODO preference
	private final boolean sortable;

	public UUID getSession() {
		return session;
	}

	public void setSession(UUID session) {
		this.session = session;
	}

	public boolean isSortable() {
		return sortable;
	}

	public static enum Sort {
		BEST, HOT, NEW, OLD, TOP, CONTROVERSIAL
	}

	public CommentListingController(final String postId) {
		this.postId = postId;
		sortable = true;
		uri = null;
	}

	public CommentListingController(final Uri uri) {
		postId = null;
		sortable = false;
		this.uri = uri;
	}

	public void setSort(final Sort s) {
		sort = s;
	}

	public URI getUri() {

		if(uri != null) {

			Uri.Builder builder = uri.buildUpon();
			if(!uri.getPath().endsWith(".json")) builder.appendPath(".json");

			try {
				return new URI(builder.toString());
			} catch(URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}

		return Constants.Reddit.getUri(Constants.Reddit.PATH_COMMENTS + postId + ".json?sort=" + sort.name().toLowerCase());
	}

	public CommentListingFragment get(final boolean force) {
		return CommentListingFragment.newInstance("t3_" + postId, getUri(), session, force ? CacheRequest.DownloadType.FORCE : CacheRequest.DownloadType.IF_NECESSARY);
	}
}